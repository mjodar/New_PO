package com.example.usuario.proyectoorlaesqueleto_ismael;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by Jose on 26/02/2017.
 */

public class MiAdapterAlumnos extends RecyclerView.Adapter<MiAdapterAlumnos.ViewHolder>{

    private Context context;
    private ArrayList<Alumno> alumnos;

    public MiAdapterAlumnos(Context context, ArrayList<Alumno> alumnos){
        this.context=context;
        this.alumnos=alumnos;
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView nombre;
        public ImageView imagen;

        public ViewHolder(View v){
            super(v);
            nombre=(TextView)v.findViewById(R.id.nombreA);
            imagen=(ImageView)v.findViewById(R.id.imagenA);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagen.buildDrawingCache();
                    Bitmap image=null;
                    Foto foto=new Foto(alumnos.get(getAdapterPosition()).getRuta());
                    try {
                        image=foto.execute().get();
                    }catch (Exception e){}

                    alumnos.get(getAdapterPosition()).setImagen(image);
                    Detalles.alumno=alumnos.get(getAdapterPosition());
                    Intent intent=new Intent(context, Detalles.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @Override
    public MiAdapterAlumnos.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v;
        v=LayoutInflater.from(context).inflate(R.layout.plantillalumno, parent, false);
        MiAdapterAlumnos.ViewHolder vh=new MiAdapterAlumnos.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nombre.setText(alumnos.get(position).getNombre());
        Bitmap image=null;
        Foto foto=new Foto(alumnos.get(position).getRuta());
        try {
            image=foto.execute().get();
        }catch (Exception e){}
        holder.imagen.setImageBitmap(image);
    }

    @Override
    public int getItemCount() {
        return alumnos.size();
    }

    public class Foto extends AsyncTask<Void, Void, Bitmap>{

        public String ruta;

        public Foto(String ruta){
            this.ruta=ruta;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.10.4.150"+ruta);
                URLConnection uc = url.openConnection();
                InputStream in = uc.getInputStream();
                Bitmap fot = BitmapFactory.decodeStream(in);
                return fot;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
