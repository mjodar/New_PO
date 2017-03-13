package com.example.usuario.proyectoorlaesqueleto_ismael;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.format.Formatter;
import android.widget.Toast;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;



public class Logi extends AsyncTask<String, Void, Alumno> {
    private String lo;
    private String pa;
    private Context ActivityActual;

    Logi(String lo,String pn, Context activity){
        this.lo=lo;
        this.ActivityActual=activity;
        this.pa=pn;
    }

    @Override
    protected Alumno doInBackground(String... params) {
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


            if(io.startsWith("10.")){
                LlenadoInternet.redcentro=true;
            }else{
                io="www.iesmurgi.org:3306";
                LlenadoInternet.redcentro=false;
            }

            Connection con;
            Class.forName("com.mysql.jdbc.Driver");
            //if(LlenadoInternet.redcentro==true) {
                con=DriverManager.getConnection("jdbc:mysql://10.10.4.150/base20173", "ubase20173", "pbase20173");
            //}else{
              //  con = DriverManager.getConnection("jdbc:mysql://www.iesmurgi.org:3306/base20173", "ubase20173", "pbase20173");
            //}

            Statement coger = con.createStatement();           

            String sql="select * from USUARIOS where user='"+lo+"'";

            ResultSet resultSet= coger.executeQuery(sql);
            Alumno alumno=null;


            if(!resultSet.next()){
                System.out.println("no hay nada");

                Handler handler =  new Handler(ActivityActual.getMainLooper());
                handler.post( new Runnable(){
                    public void run(){
                        Toast.makeText(ActivityActual, "Error en el login",Toast.LENGTH_LONG).show();
                    }
                });

            }else {
                String uno=resultSet.getString(1);
                String dos=resultSet.getString(2);
                String tres=resultSet.getString(3);
                if(dos.equalsIgnoreCase(pa)){
                    System.out.println("resultSet");
                    alumno=new Alumno("", uno, tres);
                }else{
                    Handler handler =  new Handler(ActivityActual.getMainLooper());
                    handler.post( new Runnable(){
                        public void run(){
                            Toast.makeText(ActivityActual, "Error en el login",Toast.LENGTH_LONG).show();
                        }
                    });
                }


            }
            con.close();
            return alumno;

        } catch (Exception e) {
            System.out.println("a"+e.toString());

        }

        return null;
    }
}
