package tn.esprit.babalioua;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfFormXObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class pdf extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;
    EditText txt_pdf;
    Button btn_pdf;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);

        try {
            createPdf();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        txt_pdf = findViewById(R.id.txt_pdf);
        btn_pdf = findViewById(R.id.btn_pdp);




    }

    private void createPdf() throws FileNotFoundException{

        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        File file = new File(pdfPath, "myPDF.pdf");
        OutputStream outputStream = new FileOutputStream(file);

       PdfWriter writer = new PdfWriter(String.valueOf(file));
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Paragraph paragraph = new Paragraph("this is your qrCode");

      //  document.add(paragraph);
        document.close();
        Toast.makeText(this, "QRcode est bien téléchargé", Toast.LENGTH_LONG).show();



    }


}