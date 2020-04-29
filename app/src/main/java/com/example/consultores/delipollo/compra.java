package com.example.consultores.delipollo;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.consultores.Utilidades;
import com.example.consultores.delipollo.adapter.AdapterAdicional;
import com.example.consultores.delipollo.appBar.AppBarStateChangeListener;
import com.example.consultores.delipollo.bd.bd;
import com.example.consultores.delipollo.util.Exist;
import com.example.consultores.delipollo.util.SectionOrRow;
import com.example.consultores.delipollo.util.comprasProd;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.consultores.delipollo.appBar.AppBarStateChangeListener.State.COLLAPSED;
import static com.example.consultores.delipollo.appBar.AppBarStateChangeListener.State.EXPANDED;
import static com.example.consultores.delipollo.appBar.AppBarStateChangeListener.State.IDLE;

public class compra extends AppCompatActivity
        implements AdapterAdicional.EventoCambio   {
    String codCate , codPlatillo,NamePlatillo,UrlImagen,precioPlatillo,Detalle;
    Button btn_compra ;
    ImageView img ;
    TextView txt_namePlatillo,txt_precio_platillo ,txt_detalle,txt_coment;
    RequestQueue queue ;
    ArrayList <String> NamesAdicional  = new ArrayList<>();
    double acu = 0 ;
    bd obj ;
    ArrayList<String> idSeleccionado = new ArrayList<String>();
    ArrayList<comprasProd>  listacompra = new ArrayList<>();
    //toolbar
    CollapsingToolbarLayout CollapsingToolbarLayout;
    AppBarLayout appBarLayout;
    Toolbar toolbar;



    AdapterAdicional adapter ;
    RecyclerView recycle;
    ArrayList<comprasProd> listaCompras= new ArrayList<>() ;
    ArrayList<SectionOrRow> ListSectionOrRow= new ArrayList<>() ;
    Boolean edit = false;
    int GrupoVenta  ;
    int cantidadAdicionales = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compra);
        try{
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                obj = new bd(this);
            setTitle("");

            if (getIntent().getExtras().getBoolean("crear")) {
                    codCate = getIntent().getExtras().getString("categoria");
                    codPlatillo = getIntent().getExtras().getString("codigoPlatillo");
                    NamePlatillo = getIntent().getExtras().getString("NamePlatillo");
                    UrlImagen = getIntent().getExtras().getString("urlImagePlatillo");
                    precioPlatillo = getIntent().getExtras().getString("Precio");
                    Detalle = getIntent().getExtras().getString("Detalle");
                    init();
                    GetData();
                    GetNameAdicionales();
                    toolbar();
            }
                else if((getIntent().getExtras().getBoolean("edit"))){
                this.edit =true;
                CargarProductos((getIntent().getExtras().getInt("GrupoVenta")));
                }



        }
        catch (Exception e){
            Log.d("Error",e.getMessage());
        }


    }

    private void init() {
        //ELEMENTOS
        recycle=findViewById(R.id.recyclerView);

        img = (ImageView) findViewById(R.id.img);
        txt_namePlatillo = (TextView) findViewById(R.id.txt_titulo_platillo);
        txt_precio_platillo = (TextView) findViewById(R.id.txt_precio);
        txt_detalle = (TextView) findViewById(R.id.txt_detalle);
        txt_coment = (TextView) findViewById(R.id.txt_coment);
        btn_compra = (Button) findViewById(R.id.btn_compra);
        Masyuculas();
        //setUpRecyclerView();
    }



    private void toolbar() {
        initToolbar();
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);}

        //  CollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        CollapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                if(state==EXPANDED || state==IDLE){
                    CollapsingToolbarLayout.setTitle("");
                    txt_precio_platillo.setVisibility(View.VISIBLE);

                }
                else if(state==COLLAPSED){
                    CollapsingToolbarLayout.setTitle(NamePlatillo);
                    txt_precio_platillo.setVisibility(View.GONE);

                }

            }
        });

    }

    private void initToolbar() {
        CollapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.CollapsingToolbarLayout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);

        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void CargarProductos(int grupoVenta) {
        Boolean isProductoPrincipal ;
Cursor cursor =
                obj.ReadTableWhere
                (Utilidades.table_venta,
                  Utilidades.venta_campo_grupo,String.valueOf(grupoVenta));


        while (cursor.moveToNext())
        {

            comprasProd cp   = new comprasProd();
            cp.setIdCarrito(cursor.getString(0));
            cp.setCodigoCategoria(cursor.getString(1));
            cp.setCodigo(cursor.getString(2));
            cp.setDescripcion(cursor.getString(3));
            cp.setDetalle(cursor.getString(4));
            cp.setPrecio(cursor.getString(5));
            cp.setCantidad(cursor.getString(6));
            cp.setMonto(cursor.getString(7));
            cp.setObservacion(cursor.getString(8));
            cp.setImagen(cursor.getString(9));
            cp.setFecha(cursor.getString(10));
            cp.setGrupoVenta(cursor.getInt(11));


            isProductoPrincipal = Evaluar(cursor.getString(4));
            if(isProductoPrincipal){
                Asignar(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(9),
                        cursor.getString(5),
                        cursor.getString(4),
                        cursor.getInt(11)

                        );

            }

            listacompra.add(cp);
        }


    }

    private void Asignar(String codCate,
                         String codPlatillo,
                         String NamePlatillo,
                         String UrlImagen,
                         String precioPlatillo,
                         String Detalle,
                         int grupoVenta) {

        this.codCate = codCate;
        this.codPlatillo = codPlatillo;
        this.NamePlatillo = NamePlatillo;
        this.UrlImagen = UrlImagen;
        this.precioPlatillo = precioPlatillo;
        this.Detalle = Detalle;
        this.GrupoVenta = grupoVenta;
        init();
        GetData();
        GetNameAdicionales();
        toolbar();

    }



    private boolean Evaluar(String detalle) {
        if (detalle.equals("ADICIONALES")) {
            return false ;
        }
    else{
            return true;
        }
    }


    private void Masyuculas() {
        txt_coment.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
    }

    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_venta_realizada);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        FrameLayout mDialogNo = (FrameLayout) dialog.findViewById(R.id.frmNo);
        mDialogNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent siguiente   = new Intent(getApplicationContext(),carrito.class);
                startActivity(siguiente);

            }
        });

        FrameLayout mDialogOk = (FrameLayout) dialog.findViewById(R.id.frmOk);
        mDialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent siguiente   = new Intent(getApplicationContext(),categorias.class);
                startActivity(siguiente);
                dialog.cancel();
            }
        });

        dialog.show();
    }




    public void GetNameAdicionales(){
        String url = Utilidades.server+"api/public/api/adicionalName/"+codCate;
        Log.d("url",url);
        final ProgressDialog cargando  = ProgressDialog.show(
                this,"Cargando Informacion ....",
                "Por Favor Espere ..",
                false,false );

        queue = Volley.newRequestQueue(this);  // this = context
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cargando.dismiss();

                            JSONArray Jarray = response.getJSONArray("data");
                            cantidadAdicionales = Jarray.length();
                            if(cantidadAdicionales >  0) {
                                for (int i = 0; i < Jarray.length(); i++) {
                                    JSONObject json = Jarray.getJSONObject(i);
                                    Log.d("AD ->", json.get("DescCat").toString());
                                    NamesAdicional.add(json.get("DescCat").toString());
                                }
                                GetAdicionales(NamesAdicional);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cargando.dismiss();
                        Log.d("Error.Response", error.getMessage());
                    }
                }

        );

        queue.add(getRequest);


    }


    public void GetAdicionales(final ArrayList<String> Name){
        final ArrayList<JSONArray> listaArray =  new ArrayList<JSONArray>();
        String url = Utilidades.server+"api/public/api/adicional/"+codCate;
        final ProgressDialog cargando  = ProgressDialog.show(
                this,"Cargando Informacion ....",
                "Por Favor Espere ..",
                false,false );

        queue = Volley.newRequestQueue(this);  // this = context
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            cargando.dismiss();

                            for (int i = 0; i <Name.size() ; i++) {
                                JSONArray Jarray = response.getJSONArray(Name.get(i));
                                listaArray.add(Jarray);
                            }


                                addRadioButtons(Name,listaArray);



                        } catch (Exception e) {
                            cargando.dismiss();
                            e.printStackTrace();

                        }


                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        cargando.dismiss();
                        Log.d("Error.Response", error.getMessage());
                    }
                }

        );

        queue.add(getRequest);


    }

    private void addRadioButtons(final ArrayList<String> name, final ArrayList<JSONArray> listaArray) {
        try {


            for (int j = 0; j < name.size(); j++) {
                SectionOrRow sectionOrRow = new SectionOrRow();

                ListSectionOrRow.add(sectionOrRow.createSection(name.get(j)));

                for (int k = 0; k < listaArray.get(j).length(); k++) {

                    JSONObject json = listaArray.get(j).getJSONObject(k);
                    comprasProd  cp = new comprasProd();
                    cp.setCodigoCategoria(json.get("codigo_categoria").toString());
                    cp.setDescripcionCategoria(json.get("DescCat").toString());
                    cp.setCodigo(json.get("codigo").toString());
                    cp.setDescripcion(json.get("descripcion").toString());
                    cp.setPrecio(json.get("precio").toString());

                    if (edit) {
                        int codigo = Integer.parseInt(json.get("codigo").toString());
                        Exist exist = Existe(codigo);
                       if (exist.getEstado()){
                            int index = exist.getPosition();
                            cp.setCantidad(listacompra.get(index).getCantidad());
                            cp.setMonto(listacompra.get(index).getMonto());
                        }else{
                           cp.setCantidad("0");
                           cp.setMonto("0");
                       }

                    }
                        else{
                            cp.setCantidad("0");
                            cp.setMonto("0");
                        }


                    cp.setImagen(json.get("imagen").toString());
                    ListSectionOrRow.add(sectionOrRow.createRow(cp));
                }

            }

                adaptar(ListSectionOrRow);





        }catch (Exception e ){
            Log.d("error ->",e.getMessage());
        }

    }



    private void adaptar(ArrayList<SectionOrRow> listSectionOrRow) {
            adapter= new AdapterAdicional(listSectionOrRow,this);
            recycle.setLayoutManager(new GridLayoutManager(this,1));



          /*  adapter.setOnclickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int  IdGrupoVenta =   listacompra.get(recycle.getChildAdapterPosition(v)).getGrupoVenta();
               /* Utilidades.Toast(getApplicationContext(),
                        String.valueOf(IdGrupoVenta)
                        );
                    Editar(IdGrupoVenta);


                }
            });*/
            recycle.setAdapter(adapter);
        if (this.edit){adapter.ImporteAdicionalTotal();}

    }

    private Exist Existe(int codigo) {
        Exist exist = new Exist();
        for (int j = 0; j <listacompra.size(); j++) {
            if (codigo == Integer.parseInt(listacompra.get(j).getCodigo())){
                exist.setEstado(true);
                exist.setPosition(j);
                break;
            }else{
                exist.setEstado(false);
            }
        }

        return  exist;
    }





    private void GetData() {
        Double precio = Double.parseDouble(precioPlatillo);
        acu+=precio;

        txt_namePlatillo.setText(NamePlatillo);
        txt_detalle.setText(Detalle);
        txt_precio_platillo.setText("S./" + Utilidades.Format(precioPlatillo) );
        PrintButton(acu);

        Picasso.with(this).load(UrlImagen)
                .placeholder(R.drawable.ic_ca_red).
                error(R.drawable.ic_ca_red).
                into(img);

    }

    private void PrintButton(Double acu) {
        btn_compra.setText("AGREGAR A MI ORDEN (S./ "+
                Utilidades.Format(String.valueOf(acu))
                +" )");
    }


    public void Pagar(View v){
        try{
            // si es editar
            if (this.edit){
                // borra el grupo de venta para que agrege uno nuevo
                String where = Utilidades.venta_campo_grupo + " = "+ String.valueOf(this.GrupoVenta);
                obj.Delete(Utilidades.table_venta,where);
            }


            if(cantidadAdicionales>0){
                adapter.ObjctAdcionales();
                adapter.notifyDataSetChanged();
            }



       int GrupoVenta =  Utilidades.GetSerieCorrelativoGrupoVenta(getApplicationContext());
            ContentValues valor_default  = new ContentValues();

            valor_default.put(Utilidades.venta_campo_codigoCategoria,codCate);
            valor_default.put(Utilidades.venta_campo_codigoProd,codPlatillo);
            valor_default.put(Utilidades.venta_campo_DescripcionProd,NamePlatillo);
            valor_default.put(Utilidades.venta_campo_Detalle,Detalle);
            valor_default.put(Utilidades.venta_campo_precio,precioPlatillo);
            valor_default.put(Utilidades.venta_campo_cantidad,"1");
            valor_default.put(Utilidades.venta_campo_importe,precioPlatillo);
            valor_default.put(Utilidades.venta_campo_observacion,
                    (!txt_coment.getText().toString().contentEquals("")) ? txt_coment.getText().toString() : "");
            valor_default.put(Utilidades.venta_campo_imagen,UrlImagen);
            valor_default.put(Utilidades.venta_campo_fecha,Utilidades.GetDate());
            valor_default.put(Utilidades.venta_campo_grupo,GrupoVenta);


           obj.Insert(Utilidades.table_venta,Utilidades.venta_campo_codigoVenta,valor_default);

            if (listaCompras.size()>0) {
                for (int i = 0; i < listaCompras.size(); i++) {
                    ContentValues valores  = new ContentValues();
                    valores.put(Utilidades.venta_campo_codigoCategoria,listaCompras.get((i)).getCodigoCategoria());
                    valores.put(Utilidades.venta_campo_codigoProd,listaCompras.get(i).getCodigo());
                    valores.put(Utilidades.venta_campo_DescripcionProd,listaCompras.get(i).getDescripcion());
                    valores.put(Utilidades.venta_campo_Detalle,"ADICIONALES");
                    valores.put(Utilidades.venta_campo_precio,listaCompras.get(i).getPrecio());
                    valores.put(Utilidades.venta_campo_cantidad,listaCompras.get(i).getCantidad());
                    valores.put(Utilidades.venta_campo_importe,listaCompras.get(i).getMonto());
                    valores.put(Utilidades.venta_campo_observacion,"");

                    valores.put(Utilidades.venta_campo_imagen,
                            Utilidades.server+
                            listaCompras.get(
                            i).getImagen());


                    valores.put(Utilidades.venta_campo_fecha,Utilidades.GetDate());
                    valores.put(Utilidades.venta_campo_grupo,GrupoVenta);

                    obj.Insert(Utilidades.table_venta,Utilidades.venta_campo_codigoVenta,valores);
                }
            }

            obj.CerrarConexion();

            Utilidades.UpdateSerieCorrelativoGrupoVenta(getApplicationContext(),GrupoVenta);
            showDialog();
        }catch (Exception e){
            Log.d("Error",e.getMessage().toString());
        }
    }


    @Override
    public void ListaAcionalTerminada(ArrayList<comprasProd> list) {
       if (list.size()>0) {
           this.listaCompras = list;
       }
    }

    @Override
    public void Update() {
        recycle.post(new Runnable()
        {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void importe(double acu) {
        Double neto = this.acu+acu;
        PrintButton(neto);
    }


}


