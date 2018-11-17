package macay.maceda.reloj.checadortrial;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import macay.maceda.reloj.checadortrial.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checadortrial.Model.Empleados_admin;

public class UserPanelActivity extends AppCompatActivity {
    private long receivedPersonId;
    private DatabaseOpenHelper dbHelper;
    private CardView workin, workout, workback;
    TextView chekin_tv, checkout_tv, breakin_tv, breakout_tv;
    FloatingActionButton bt;
    CircleImageView imagen;
    TextView nombres;
    private String mCurrentPhotoPath = "";
    private String mWorkin, mWorkout, mBreakin, mBreakout,formatdate;
    //private Handler _handler;
    private static final Handler handler = new Handler();

    private int screenWidth;
    String valorformatdate=null;
    String valorguardado=null;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_panel);
        SharedPreferences prefs = getSharedPreferences("datos", MODE_PRIVATE);
        valorformatdate = prefs.getString("formatDate",null);
        getSupportActionBar().hide();
        dbHelper = new DatabaseOpenHelper(this);
        imagen = (CircleImageView) findViewById(R.id.avatar);
        nombres = (TextView) findViewById(R.id.my_name);
        bt = (FloatingActionButton) findViewById(R.id.b_edit);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_pin_dialog();
            }
        });
        workin = (CardView) findViewById(R.id.inicio_trabajo);
        workout = (CardView) findViewById(R.id.salida_trabajo);
        workback = (CardView) findViewById(R.id.regreso_trabajo);

        chekin_tv = (TextView) findViewById(R.id.checkin_tv);
        checkout_tv = (TextView) findViewById(R.id.checkout_tv);
        breakin_tv = (TextView) findViewById(R.id.breakin_tv);
        breakout_tv = (TextView) findViewById(R.id.breakout_tv);


        Cursor mc = dbHelper.get_all_activitys();

        if (mc.getCount() > 9) {
            download_full_app();
        }

        try {
            receivedPersonId = getIntent().getLongExtra("USER_ID", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);
        mCurrentPhotoPath = receivedPerson.getImage();
        nombres.setText(receivedPerson.getName()+" "+ receivedPerson.getLastname());

        Cursor cursor = dbHelper.already_workin_today(String.valueOf(receivedPersonId),"" );

        if(cursor.getCount() > 0) {



            if (cursor.moveToLast()) {// data?
                valorguardado = null;
                mWorkin = cursor.getString(cursor.getColumnIndex("workin"));
                mWorkout = cursor.getString(cursor.getColumnIndex("workout"));
                mBreakin = cursor.getString(cursor.getColumnIndex("breakin"));
                mBreakout = cursor.getString(cursor.getColumnIndex("breakout"));
                formatdate = cursor.getString(cursor.getColumnIndex("sdate"));
                valorguardado = cursor.getString(cursor.getColumnIndex("formatdate"));

                //cursor.getString(0)

                    /*
                    Toast.makeText(UserPanelActivity.this,
                            "ENTRADA: " + mWorkin,
                            Toast.LENGTH_SHORT).show();
                            */
                chekin_tv.setVisibility(View.VISIBLE);
                chekin_tv.setText(getString(R.string.entrada)+" :" +mWorkin);

                if (mWorkout == null) {
                    //la salida aun no fue registrada
                    workin.setVisibility(View.GONE);
                    workout.setVisibility(View.VISIBLE);
                    checkout_tv.setVisibility(View.GONE);

                } else {
                    //la salida ya fue registrada
                    workout.setVisibility(View.GONE);
                    workin.setVisibility(View.VISIBLE);
                    checkout_tv.setVisibility(View.VISIBLE);
                    checkout_tv.setText(getString(R.string.salida)+" :" + mWorkout);
                }

                if (mBreakout != null) {
                    breakout_tv.setVisibility(View.VISIBLE);
                    breakout_tv.setText(getString(R.string.salida_comer) +mBreakout);
                    workout.setVisibility(View.GONE);
                    workback.setVisibility(View.VISIBLE);
                }

                if (mBreakin != null) {
                    breakin_tv.setVisibility(View.VISIBLE);
                    breakin_tv.setText(getString(R.string.regreso_trabajo) +mBreakin);

                    if (mWorkout == null) {
                        workin.setVisibility(View.GONE);
                        workout.setVisibility(View.VISIBLE);
                        workback.setVisibility(View.GONE);
                    }
                    else {
                        workout.setVisibility(View.GONE);
                        workin.setVisibility(View.VISIBLE);
                        workback.setVisibility(View.GONE);
                    }

                } //la llegada a comer aun no esta registrada

            }

        }
        else {
            workout.setVisibility(View.GONE);
            workin.setVisibility(View.VISIBLE);

        }

        int height;
        if (this.receivedPersonId == 1 || receivedPerson.getImage() == null) {
            height = 700;
        } else {
            height = 600;
        }
        Picasso.with(this)
                .load(new File(receivedPerson.getImage()))
                .resize(screenWidth / 2, height)
                .placeholder(R.drawable.persona)
                .into(imagen);

        workback.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWorkout == null){
                    dbHelper.insert_user_breakin(String.valueOf(receivedPersonId), mWorkin, datetimexx(valorguardado), UserPanelActivity.this);
                }else{
                    dbHelper.insert_user_breakin(String.valueOf(receivedPersonId), mWorkin, datetimexx(valorformatdate), UserPanelActivity.this);
                }
                finish();

            }


        });

        workin.setOnClickListener(  new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWorkout == null){
                    if (valorguardado!=null){
                        dbHelper.insert_user_workin(receivedPersonId, datex(), datetimexx(valorguardado),valorguardado);
                    }else{
                        dbHelper.insert_user_workin(receivedPersonId, datex(), datetimexx(valorformatdate),valorformatdate);
                    }
                }else{
                    dbHelper.insert_user_workin(receivedPersonId, datex(), datetimexx(valorformatdate),valorformatdate);
                }
                Toast.makeText(UserPanelActivity.this,
                        getString(R.string.entrada_registrada),
                        Toast.LENGTH_LONG).show();
                finish();

            }
        });





        workout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(UserPanelActivity.this,
                //      "userid=" + String.valueOf(receivedPersonId) + " date=" +datex(),
                //    Toast.LENGTH_SHORT).show();


                if (mBreakin == null) {
                    options_exit();
                }
                else {
                    if (mWorkout == null){
                        dbHelper.insert_user_workout(String.valueOf(receivedPersonId), mWorkin, datetimexx(valorguardado), UserPanelActivity.this );
                    }else{
                        dbHelper.insert_user_workout(String.valueOf(receivedPersonId), mWorkin, datetimexx(valorformatdate), UserPanelActivity.this );
                    }
                    Toast.makeText(UserPanelActivity.this,getString(R.string.culminacion_trabajo_exitosa),Toast.LENGTH_LONG).show();
                    finish();
                }

            }
        });
    }

    private void edit_pin_dialog() {
        handler.removeCallbacks(textRunnable);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserPanelActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_admin,null);
        final EditText password = (EditText) mView.findViewById(R.id.pass);
        final EditText repeatpassword = (EditText) mView.findViewById(R.id.repeatpass);
        password.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        repeatpassword.setInputType(InputType.TYPE_CLASS_NUMBER |InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        password.setHint(getString(R.string.nuevo_pin));
        repeatpassword.setHint(getString(R.string.repita_pin));
        TextView tv = (TextView) mView.findViewById(R.id.textView);
        //  tv.setVisibility(View.GONE);
        tv.setText(getString(R.string.cambiar_pin));
        Button cancel = (Button) mView.findViewById(R.id.cancel);
        Button register = (Button) mView.findViewById(R.id.login);

        mBuilder.setView(mView);
        //mBuilder.setTitle("Crear una contraseña");
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                handler.postDelayed(textRunnable, 10000);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Empleados_admin receivedPerson = dbHelper.getPerson(receivedPersonId);

                if (!password.getText().toString().isEmpty() && !repeatpassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().equals(repeatpassword.getText().toString())) {
                        if (password.length() < 4 && repeatpassword.length() < 4){
                            password.setError(getString(R.string.agregue_4_numeros));
                            repeatpassword.setError(getString(R.string.agregue_4_numeros));
                        }else {
                            Empleados_admin updatedPerson = new Empleados_admin(receivedPerson.getName(),
                                    receivedPerson.getLastname(),receivedPerson.getNumber_phone(),
                                    receivedPerson.getOccupation(),receivedPerson.getArea(),
                                    receivedPerson.getEmail(), receivedPerson.getBirthday(),receivedPerson.getAddress(),
                                    receivedPerson.getDatework(),mCurrentPhotoPath, receivedPerson.getBlocked(), password.getText().toString().trim());
                            dbHelper.updatePasswordPerson(receivedPersonId, UserPanelActivity.this, updatedPerson);
                            dialog.dismiss();
                            handler.postDelayed(textRunnable, 10000);
                        }




                    } else {
                        repeatpassword.setError(getString(R.string.clave_no_coinciden));
                    }
                } else {
                    Toast.makeText(UserPanelActivity.this,
                            getString(R.string.ingresar_campos),
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }







    private void options_exit () {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserPanelActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_option_exit, null);

        ImageView person_culminate = (ImageView) mView.findViewById(R.id.culminate);
        ImageView person_food = (ImageView) mView.findViewById(R.id.food);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        person_culminate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWorkout == null){
                    dbHelper.insert_user_workout(String.valueOf(receivedPersonId), mWorkin, datetimexx(valorguardado),UserPanelActivity.this );
                }else{
                    dbHelper.insert_user_workout(String.valueOf(receivedPersonId), mWorkin, datetimexx(valorformatdate),UserPanelActivity.this );
                }
                Toast.makeText(UserPanelActivity.this,getString(R.string.culminacion_trabajo_exitosa),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });

        person_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mWorkout == null){
                    dbHelper.insert_user_breakout(String.valueOf(receivedPersonId), mWorkin, datetimexx(valorguardado),UserPanelActivity.this );
                }else{
                    dbHelper.insert_user_breakout(String.valueOf(receivedPersonId), mWorkin, datetimexx(valorformatdate),UserPanelActivity.this );
                }
                Toast.makeText(UserPanelActivity.this,getString(R.string.hora_comer),Toast.LENGTH_LONG).show();
                dialog.dismiss();
                finish();
            }
        });
    }
    public static String datex () {
        Date date = Calendar.getInstance().getTime();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String today = formatter.format(date);

        return today;
    }


    public static String datetime () {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String today = formatter.format(date);

        return today;
    }

    public static String datetimexx (String typeformat) {
        Date date = Calendar.getInstance().getTime();
        DateFormat formatter = new SimpleDateFormat(typeformat+" hh:mm:ss");
        String today = formatter.format(date);

        return today;
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        handler.postDelayed(textRunnable, 10000);

    }

    private final Runnable textRunnable = new Runnable() {
        @Override
        public void run() {
            finish();
        }
    };

    private void download_full_app () {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(UserPanelActivity.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_play_store, null);
        Button yes = (Button) mView.findViewById(R.id.si);
        Button no = (Button) mView.findViewById(R.id.no);
        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();
        dialog.setCancelable(false);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=macay.maceda.reloj.checador");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                finish();
            }
        });
    }

}
