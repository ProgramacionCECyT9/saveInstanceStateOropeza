package com.escuela.reserva;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TimePicker;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnSeekBarChangeListener,
		OnClickListener, OnDateSetListener, OnTimeSetListener {

	EditText nombre, cantidadDolar;
	TextView cuantasPersonas, texto;
	Button fecha, hora;
	SeekBar barraPersonas;

	SimpleDateFormat horaFormato, fechaFormato;
	MediaPlayer mp;

	String nombreReserva = "";
	String numPersonas = "";
	String fechaSel = "", horaSel = "";
	Date fechaConv;
	String cuantasPersonasFormat = "";
	int personas = 1; // Valor por omision, al menos 1 persona tiene que reservar

	Calendar calendar = Calendar.getInstance();

	int contador = 0;
	Date fechaReservacion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		Toast.makeText(this, "onCreate", Toast.LENGTH_SHORT).show();

//		mp = MediaPlayer.create(this, R.raw.audio);
//		mp.start();

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		texto = (TextView) findViewById(R.id.textViewText);
        cantidadDolar = (EditText) findViewById(R.id.editTextCantidad);

		cuantasPersonas = (TextView) findViewById(R.id.cuantasPersonas);
		barraPersonas = (SeekBar) findViewById(R.id.personas);

		fecha = (Button) findViewById(R.id.fecha);
		hora = (Button) findViewById(R.id.hora);
		fecha.setOnClickListener(this);
		hora.setOnClickListener(this);

		barraPersonas.setOnSeekBarChangeListener(this);
		nombre = (EditText) findViewById(R.id.nombre);

		cuantasPersonasFormat = cuantasPersonas.getText().toString();
		// cuantasPersonasFormat = "personas: %d";
		cuantasPersonas.setText("Personas: 1"); // condicion inicial

		// Para seleccionar la fecha y la hora

		// formatos de la fecha y hora
		fechaFormato = new SimpleDateFormat("yyyy-MM-dd");
		horaFormato = new SimpleDateFormat("HH:mm");
		// horaFormato = new SimpleDateFormat(hora.getText().toString());
		try {
			Bundle date_bundle = savedInstanceState.getBundle("date");
			Bundle hour_bundle = savedInstanceState.getBundle("hour");
			int hour = hour_bundle.getInt("hour");
			calendar.set(date_bundle.getInt("year"),
					date_bundle.getInt("month"),
					date_bundle.getInt("day"),
					hour_bundle.getInt("hour"),
					hour_bundle.getInt("minute"));
			Date user_date = calendar.getTime();
			fecha.setText(fechaFormato.format(user_date));
			hora.setText(horaFormato.format(user_date));
		} catch (Exception e){
			fecha.setText(fechaFormato.format(calendar.getTime()));
			calendar.set(Calendar.HOUR_OF_DAY, 12); // hora inicial
			calendar.clear(Calendar.MINUTE); // 0
			horaSel = horaFormato.format(calendar.getTime());
			hora.setText(horaSel);
		}


	}

	@Override protected void onStart() {
		super.onStart();
		Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
	}

	@Override protected void onResume() {
		super.onResume();
		Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
//		mp.start();
	}

	@Override protected void onPause() {
		Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
		super.onPause();
//		mp.pause();
	}

	@Override protected void onStop() {
		super.onStop();
		Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
	}

	@Override protected void onRestart() {
		super.onRestart();
		Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
	}

	@Override protected void onDestroy() {
//	    mp.stop();
		super.onDestroy();
		Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();

	}

	public void lanzarAcercaDe(View view){
		Intent i = new Intent(this, AcercaDe.class);
		startActivity(i);
	}

	public void pulso(View view)
	{
		contador++;
		texto.setText(String.valueOf(contador));
	}


	@Override
	protected void onSaveInstanceState(Bundle guardarEstado) {
		super.onSaveInstanceState(guardarEstado);
		guardarEstado.putInt("contador", contador);
		int amountPeopleBarProgress =  barraPersonas.getProgress();
		guardarEstado.putInt("personas", amountPeopleBarProgress);

		Calendar calendar = fechaFormato.getCalendar();
		Bundle date_bundle = new Bundle();
		date_bundle.putInt("year", calendar.get(Calendar.YEAR));
		date_bundle.putInt("month", calendar.get(Calendar.MONTH));
		date_bundle.putInt("day", calendar.get(Calendar.DAY_OF_MONTH));
		guardarEstado.putBundle("date", date_bundle);

		calendar = horaFormato.getCalendar();
		Bundle hour_bundle = new Bundle();
		hour_bundle.putInt("hour", calendar.get(Calendar.HOUR_OF_DAY));
		hour_bundle.putInt("minute", calendar.get(Calendar.MINUTE));
		guardarEstado.putBundle("hour", hour_bundle);
	}

	@Override
	protected void onRestoreInstanceState(Bundle recEstado) {
		super.onRestoreInstanceState(recEstado);
		contador = recEstado.getInt("contador");
		texto.setText(String.valueOf(contador));
		int amountPeopleBarProgress = recEstado.getInt("personas");
		cuantasPersonas.setText(String.valueOf(amountPeopleBarProgress));
	}


	@Override
	public void onProgressChanged(SeekBar barra, int progreso,
			boolean delUsuario) {

		numPersonas = String.format(cuantasPersonasFormat,
				barraPersonas.getProgress() + 1);
		personas = barraPersonas.getProgress() + 1; // este es el valor que se
													// guardara en la BD
		// Si no se mueve la barra, enviamos el valor personas = 1
		cuantasPersonas.setText(numPersonas);
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
	}

	@Override
	public void onClick(View v) {
		if (v == fecha) {
			Calendar calendario = parseCalendar(fecha.getText(), fechaFormato);
			new DatePickerDialog(this, this, calendario.get(Calendar.YEAR),
					calendario.get(Calendar.MONTH),
					calendario.get(Calendar.DAY_OF_MONTH)).show();
		} else if (v == hora) {
			Calendar calendario = parseCalendar(hora.getText(), horaFormato);
			new TimePickerDialog(this, this,
					calendario.get(Calendar.HOUR_OF_DAY),
					calendario.get(Calendar.MINUTE), false) // /true = 24 horas
					.show();
		}
	}

	private Calendar parseCalendar(CharSequence text,
			SimpleDateFormat fechaFormat2) {
		try {
			fechaConv = fechaFormat2.parse(text.toString());
		} catch (ParseException e) { // import java.text.ParsedExc
			throw new RuntimeException(e);
		}
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fechaConv);
		return calendario;
	}

	@Override
	public void onDateSet(DatePicker picker, int anio, int mes, int dia) {
		calendar.set(Calendar.YEAR, anio);
		calendar.set(Calendar.MONTH, mes);
		calendar.set(Calendar.DAY_OF_MONTH, dia);

		fechaSel = fechaFormato.format(calendar.getTime());
		fecha.setText(fechaSel);

	}

	public void onTimeSet(TimePicker picker, int horas, int minutos) {
		calendar.set(Calendar.HOUR_OF_DAY, horas);
		calendar.set(Calendar.MINUTE, minutos);

		horaSel = horaFormato.format(calendar.getTime());
		hora.setText(horaSel);
	}

	public void reserva(View v) {
		Intent envia = new Intent(this, Actividad2.class);
		Bundle datos = new Bundle();
		datos.putString("nombre", nombre.getText().toString().trim());
		datos.putInt("personas", personas);
		datos.putString("fecha", fechaSel);
		datos.putString("hora", horaSel);
		datos.putInt("dolares", (Integer.parseInt(cantidadDolar.getText().toString().trim()) / 15));
		envia.putExtras(datos);
		finish();
		startActivity(envia);
	}
}
