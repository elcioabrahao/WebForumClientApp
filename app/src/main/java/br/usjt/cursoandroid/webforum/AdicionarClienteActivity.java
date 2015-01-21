package br.usjt.cursoandroid.webforum;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.usjt.cursoandroid.webforum.entity.Cliente;
import br.usjt.cursoandroid.webforum.helper.Http;


public class AdicionarClienteActivity extends Activity implements Runnable{

    private EditText ed;
    private Cliente cliente;
    private String nome;
    private ProgressDialog dialog;
    private final String URL="http://www.qpainformatica.com.br/WebForum/Registrar";
    private String retorno="";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_cliente);

        ImageButton ok = (ImageButton)findViewById(R.id.imageButtonOk);
        ed = (EditText)findViewById(R.id.editTextNome);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nome = ed.getText().toString();

                if(nome.length()<3){
                    Toast.makeText(AdicionarClienteActivity.this,"O nome deve ter pelo menos 3 caracteres !",Toast.LENGTH_SHORT).show();
                }else{

                    dialog = ProgressDialog.show(AdicionarClienteActivity.this, "Registro no Forum", "Registrando cliente no forum, aguarde...", false, true);
                    new Thread(AdicionarClienteActivity.this).start();
                }


            }
        });

    }


    public void run() {


        try {

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("nome", nome);
            retorno = Http.getInstance(Http.NORMAL).doPost(URL, params);

        } catch (Exception e) {
            Log.e("REGISTRO", e.getMessage(), e);
            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(AdicionarClienteActivity.this,"Problema de conexão com a Internet, tente mais tarde",Toast.LENGTH_SHORT).show();
                }
            });

        } finally {


            dialog.dismiss();
            try{
                int id = Integer.parseInt(retorno);
                Intent returnIntent = new Intent();
                // TODO gravar os dados do cliente aqui e colocar o resultado no putextra
                Cliente cliente = new Cliente();
                cliente.setId(id);
                cliente.setNome(nome);
                // instanciar DAO e fazer insert do cliente
                returnIntent.putExtra("cliente",cliente);
                setResult(id,returnIntent);
                finish();
            }catch(NumberFormatException e){
                handler.post(new Runnable() {
                    public void run() {
                        Toast.makeText(AdicionarClienteActivity.this,"Não foi possível registrar o cliente, tente mais tarde",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }
    }

}
