package com.github.quadflask.fleamarketseller.view;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.quadflask.fleamarketseller.R;
import com.github.quadflask.fleamarketseller.model.Vendor;
import com.google.common.base.Strings;

import butterknife.BindView;
import butterknife.OnClick;
import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmViewHolder;
import lombok.val;

import static com.github.quadflask.fleamarketseller.FleamarketApplication.actionCreator;
import static com.github.quadflask.fleamarketseller.FleamarketApplication.store;

public class VendorListActivity extends BaseActivity implements OnClickEditListener<Vendor> {
	@BindView(R.id.main_content)
	CoordinatorLayout llRoot;
	@BindView(R.id.toolbar)
	Toolbar toolbar;
	@BindView(R.id.fab)
	FloatingActionButton fab;
	@BindView(R.id.rv_list)
	RealmRecyclerView rvList;

	private RealmBasedRecyclerViewAdapter<Vendor, VendorViewHolder> adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setToolbar(toolbar);
	}

	@Override
	protected void onResume() {
		super.onResume();
		reloadVendors();
	}

	private void reloadVendors() {
		val vendors = store().loadVendors();
		if (adapter == null) {
			adapter = new RealmBasedRecyclerViewAdapter<Vendor, VendorViewHolder>(this, vendors, true, false) {
				@Override
				public VendorListActivity.VendorViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
					VendorListActivity.VendorViewHolder viewHolder = new VendorListActivity.VendorViewHolder(inflater.inflate(VendorListActivity.VendorViewHolder.RES_ID, viewGroup, false));
					viewHolder.root.setOnClickListener(v -> VendorListActivity.this.onClickEdit(viewHolder.vendor));
					return viewHolder;
				}

				@Override
				public void onBindRealmViewHolder(VendorListActivity.VendorViewHolder viewHolder, int i) {
					val vendor = realmResults.get(i);
					viewHolder.vendor = vendor;
					viewHolder.name.setText(vendor.getName());
					viewHolder.location.setText(vendor.getLocation());
				}
			};
			rvList.setAdapter(adapter);
		} else adapter.updateRealmResults(vendors);
	}

	@OnClick(R.id.fab)
	void addVendor() {
		new MaterialDialog.Builder(this)
				.title("매입처 추가")
				.customView(R.layout.dialog_input_vendor, true)
				.positiveText("추가")
				.onPositive((dialog, which) -> {
					View view = dialog.getCustomView();
					EditText edName = (EditText) view.findViewById(R.id.ed_name);
					EditText edLocation = (EditText) view.findViewById(R.id.ed_location);

					if (!Strings.isNullOrEmpty(edName.getText().toString())) {
						final Vendor newVendor = Vendor.builder()
								.name(edName.getText().toString())
								.location(edLocation.getText().toString())
								.build();

						store()
								.checkValidAsObservable(newVendor)
								.subscribe(
										v -> actionCreator().newVendor(newVendor),
										e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
					} else Toast.makeText(this, "이름이 비어있습니다", Toast.LENGTH_SHORT).show();
				})
				.show();
	}

	@Override
	protected int getContentViewResId() {
		return R.layout.activity_vendor_list;
	}

	@Override
	public void onClickEdit(final Vendor targetVendor) {
		MaterialDialog dialog = new MaterialDialog.Builder(this)
				.title("수정")
				.customView(R.layout.dialog_input_vendor, true)
				.positiveText("완료")
				.onPositive((_dialog, which) -> {
					View view = _dialog.getCustomView();
					EditText edName = (EditText) view.findViewById(R.id.ed_name);
					EditText edLocation = (EditText) view.findViewById(R.id.ed_location);

					val editedVendor = Vendor.builder()
							.id(targetVendor.getId())
							.name(edName.getText().toString())
							.location(edLocation.getText().toString())
							.build();

					store().checkValidAsObservable(editedVendor).subscribe(
							v -> actionCreator().editVendor(editedVendor),
							e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show()
					);
				})
				.show();

		View customView = dialog.getCustomView();
		EditText edName = (EditText) customView.findViewById(R.id.ed_name);
		EditText edLocation = (EditText) customView.findViewById(R.id.ed_location);

		edName.setText(targetVendor.getName());
		edLocation.setText(targetVendor.getLocation());
	}

	@Override
	public void onNext(UiUpdateEvent uiUpdateEvent) {
		if (uiUpdateEvent instanceof UiUpdateEvent.MarketAdded) {
			reloadVendors();
			Snackbar.make(llRoot, "매입처가 추가되었습니다", Snackbar.LENGTH_SHORT).show();
		} else if (uiUpdateEvent instanceof UiUpdateEvent.MarketUpdated) {
			reloadVendors();
			Snackbar.make(llRoot, "매입처가 수정되었습니다", Snackbar.LENGTH_SHORT).show();
		}
	}

	private static class VendorViewHolder extends RealmViewHolder {
		@LayoutRes
		static final int RES_ID = R.layout.li_vendor;

		final LinearLayout root;
		final TextView name, location;

		Vendor vendor;

		public VendorViewHolder(View itemView) {
			super(itemView);
			root = (LinearLayout) itemView.findViewById(R.id.ll_root);
			name = (TextView) itemView.findViewById(R.id.tv_name);
			location = (TextView) itemView.findViewById(R.id.tv_location);
		}
	}
}
