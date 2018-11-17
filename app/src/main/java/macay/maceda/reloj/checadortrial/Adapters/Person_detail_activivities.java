package macay.maceda.reloj.checadortrial.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import macay.maceda.reloj.checadortrial.DataBase.DatabaseOpenHelper;
import macay.maceda.reloj.checadortrial.Model.Actividades_empleados;
import macay.maceda.reloj.checadortrial.Model.Empleados_admin;
import macay.maceda.reloj.checadortrial.R;

/**
 * Created by Vlover on 06/02/2018.
 */

public class Person_detail_activivities extends RecyclerView.Adapter<Person_detail_activivities.ViewHolder> {
    private List<Actividades_empleados> mEmpleados;
    private Context mContext;
    private RecyclerView mRecyclerV;
    private DatabaseOpenHelper dbHelper;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView personId;
        TextView personUserId;
        TextView personWorking;
        TextView personWorkout;
        TextView personBreaking;
        TextView personBreakout;
        TextView totalidad;
        public View layout;

        ViewHolder(View v) {
            super(v);
            layout = v;
            personId =  (TextView) v.findViewById(R.id.a_ide);
            personUserId = (TextView) v.findViewById(R.id.user_id);
            personWorking = (TextView) v.findViewById(R.id.a_working);
            personWorkout = (TextView) v.findViewById(R.id.a_workout);
            personBreaking = (TextView) v.findViewById(R.id.a_breaking);
            personBreakout = (TextView) v.findViewById(R.id.a_breakout);
            totalidad = (TextView) v.findViewById(R.id.calculo_total);
            //personId.setVisibility(View.GONE);
            //personUserId.setVisibility(View.GONE);
        }


    }

    public void add(int position, Actividades_empleados empleados_admin) {
        mEmpleados.add(position, empleados_admin);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mEmpleados.remove(position);
        notifyItemRemoved(position);
    }



    // Provide a suitable constructor (depends on the kind of dataset)
    public Person_detail_activivities(List<Actividades_empleados> myDataset, Context context, RecyclerView recyclerView) {
        mEmpleados = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public Person_detail_activivities.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v = inflater.inflate(R.layout.item_actividades_empleados, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    public void onBindViewHolder(final Person_detail_activivities.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        dbHelper = new DatabaseOpenHelper(mContext);
        final Actividades_empleados person = mEmpleados.get(position);
        final Empleados_admin receivedPerson = dbHelper.getPerson(Long.parseLong(person.getUserid()));
       // holder.personId.setText("Numero:"+ person.getId());
        holder.personUserId.setText(mContext.getString(R.string.empleado_id) +person.getUserid());
        holder.personId.setText(receivedPerson.getName()+" "+receivedPerson.getLastname());
        holder.personId.setTextSize(20);
        holder.personWorking.setText(mContext.getString(R.string.inicio_trabajo) +person.getWorking());
        if (person.getWorkout() == null ){
            holder.personWorkout.setVisibility(View.GONE);
        }else{
            holder.personWorkout.setText(mContext.getString(R.string.culminacion_trabajo)+person.getWorkout());
        }
        if (person.getBreaking() == null){
            holder.personBreakout.setVisibility(View.GONE);
        }else{
            holder.personBreakout.setText(mContext.getString(R.string.regreso_trabajo)+person.getBreaking());

        }
        if (person.getBreakout() == null){
            holder.personBreaking.setVisibility(View.GONE);
        }else{
            holder.personBreaking.setText(mContext.getString(R.string.salida_comer)+person.getBreakout());

        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


    }
    @Override
    public int getItemCount() {
        return mEmpleados.size();
    }
}