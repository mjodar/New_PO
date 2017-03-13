package com.example.usuario.proyectoorlaesqueleto_ismael;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class LlenadoInternet extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    public static boolean redcentro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler);
        Bundle bundle=getIntent().getExtras();

        ArrayList<Alumno> alumnos=new ArrayList<>();
        FloatingActionButton fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        final Context context=this.getApplicationContext();

        BD b=new BD(bundle.getString("Curso"), context);

        try {
            alumnos=b.execute().get();
        }catch (Exception e){

        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        layoutManager = new GridLayoutManager(context, 3);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MiAdapterAlumnos(context, alumnos);
        recyclerView.setAdapter(adapter);


    }

    public class BD extends AsyncTask<String, Void , ArrayList<Alumno>> {

        public ArrayList<Alumno> alumnos=new ArrayList<>();
        private String clase;
        Context context;

        public BD(String clase, Context co){
            this.clase=clase;
            context=co;
        }



        boolean redCentro=false;
        @Override
        protected ArrayList<Alumno> doInBackground(String... params) {


            try {
                String io="";

                List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
                System.out.println("Cantidad de interfaces"+interfaces.size());
                for (NetworkInterface intf : interfaces) {
                    List<InetAddress> addrs = Collections.list(intf.getInetAddresses()); 
                    for (InetAddress addr : addrs) {
                        if (!addr.isLoopbackAddress()) { 
                            String sAddr = addr.getHostAddress();
                            
                            boolean isIPv4 = sAddr.indexOf(':')<0;
                            io= Formatter.formatIpAddress(addr.hashCode());
                            System.out.println(io);
                            if (true) {
                                if (isIPv4)
                                    break;
                            }
                        }
                    }
                }

                System.out.println("Direcion ip encontrada "+io);
                if(io.startsWith("10.")){
                    redCentro=true;
                }else{
                    io="www.iesmurgi.org:3306";
                    redCentro=false;
                }

                Connection con;

                Class.forName("com.mysql.jdbc.Driver");


                con = DriverManager.getConnection("jdbc:mysql://10.10.4.150/base20173", "ubase20173", "pbase20173");


                Statement coger = con.createStatement();
                if(clase.equals("Bachiller A"))clase="BACHA";
                if(clase.equals("Bachiller B"))clase="BACHB";
                if(clase.equals("Bachiller C"))clase="BACHC";

                System.out.println(clase);
                String sql="select * from "+clase;

                ResultSet a= coger.executeQuery(sql);
                alumnos=new ArrayList<>();
             
                String arrayFiltrado="";

                while (a.next()){
                    if(!redCentro){
                        arrayFiltrado="";
                        String ruta=a.getString(4);
                        System.out.println("ruta"+ruta);
                        char[] rutaFilro=ruta.toCharArray();

                        for(int o=18;o<ruta.length();o++){
                            arrayFiltrado=arrayFiltrado+rutaFilro[o];
                        }
                       // arrayFiltrado="http://www.iesmurgi.org:85"+arrayFiltrado;
                    }
                    System.out.println("arrayfiltrado"+arrayFiltrado);
                  
                    alumnos.add(new Alumno(a.getString(2), clase, arrayFiltrado, 0));
                }
                return alumnos;

            } catch (Exception e) {
                System.out.println("a"+e.toString());
                e.printStackTrace();
            }

            return null;
        }
    }
}
