package com.github.quadflask.fleamarketseller.io;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.github.quadflask.fleamarketseller.model.Columnable;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import io.realm.RealmObject;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;

public class RealmExcelExporter {

	public Observable<Boolean> export(final RealmResults<RealmObject> result) {
		return Observable.create(new Observable.OnSubscribe<Boolean>() {
			@Override
			public void call(Subscriber<? super Boolean> subscriber) {
				subscriber.onStart();
				new Thread(() -> {
					List<Columnable> rows = Stream.of(result.iterator())
							.filter(ro -> ro instanceof Columnable)
							.map(ro -> (Columnable) ro)
							.collect(Collectors.toList());

					String[] header = rows.get(0).getFiledNames();

					try {
						writeRows(header, rows, "output.xlsx");
						subscriber.onNext(true);
					} catch (IOException e) {
						subscriber.onError(e);
						e.printStackTrace();
					}
				}).start();
			}
		});
	}

	private void writeRows(String[] header, List<Columnable> body, String filePath) throws IOException {
		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("new sheet");
		int rowIndex = 0;

		writeRow(sheet.createRow(rowIndex++), header);
		for (Columnable columnable : body)
			writeRow(sheet.createRow(rowIndex++), columnable.getData());

		FileOutputStream fileOut = new FileOutputStream(filePath);
		wb.write(fileOut);

		fileOut.close();
		wb.close();
	}

	private void writeRow(Row row, String[] data) {
		for (int i = 0; i < data.length; i++)
			row.createCell(i).setCellValue(data[i]);
	}
}
