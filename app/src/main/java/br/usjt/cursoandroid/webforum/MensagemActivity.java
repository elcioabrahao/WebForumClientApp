package br.usjt.cursoandroid.webforum;


import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import br.usjt.cursoandroid.webforum.entity.Cliente;
import br.usjt.cursoandroid.webforum.entity.Mensagem;
import br.usjt.cursoandroid.webforum.helper.Http;


public class MensagemActivity extends ListActivity implements Runnable {

    private Button botaoEnviar;
    private EditText campoMensagem;
    private Cliente cliente;
    private int idInicial=0;
    private int acesso = 0; // igual a zero para consulta de mensagens
    private static final String URL_GET_MENSAGENS="http://www.qpainformatica.com.br/WebForum/GetMensagens";
    private static final String URL_POSTAR="http://www.qpainformatica.com.br/WebForum/Postar";
    private String retorno="";
    private Handler handler = new Handler();
    private ProgressDialog dialog;
    private static final int CONSULTAR = 0;
    private static final int POSTAR = 1;
    private ArrayList<Mensagem> mensagens = new ArrayList<Mensagem>();
    ArrayList<HashMap<String, String>> mensagemList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensagem);

        botaoEnviar = (Button)findViewById(R.id.buttonEnviar);
        campoMensagem = (EditText)findViewById(R.id.novaMensagem);

        Intent intent = getIntent();
        cliente = (Cliente)intent.getSerializableExtra("cliente");

        botaoEnviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acesso=POSTAR;
                dialog = ProgressDialog.show(MensagemActivity.this, "WebForum", "Postando mensagem, aguarde...", false, true);
                new Thread(MensagemActivity.this).start();
            }
        });


        idInicial=0;
        acesso=CONSULTAR;
        dialog = ProgressDialog.show(this, "WebForum", "Carregando mensagens, aguarde...", false, true);
        new Thread(this).start();

    }

    public void run() {


        try {

            Map<String, Object> params = new HashMap<String, Object>();
            if(acesso==CONSULTAR){
                params.put("id", idInicial);
                retorno = Http.getInstance(Http.NORMAL).doPost(URL_GET_MENSAGENS, params);
            }else if (acesso==POSTAR){

                Gson gson = new Gson();
                Mensagem mensagem = new Mensagem();
                mensagem.setId(0);
                mensagem.setIdCliente(cliente.getId());
                mensagem.setNomeCliente(cliente.getNome());
                mensagem.setMensagem(campoMensagem.getText().toString());
                mensagem.setData(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
                params.put("m", gson.toJson(mensagem));
                retorno = Http.getInstance(Http.NORMAL).doPost(URL_POSTAR, params);

            }

        } catch (Exception e) {

            handler.post(new Runnable() {
                public void run() {
                    Toast.makeText(MensagemActivity.this, "Problema de conexão com a Internet, tente mais tarde", Toast.LENGTH_SHORT).show();
                }
            });

        } finally {

            dialog.dismiss();
            if(acesso==POSTAR){
                if(retorno.equalsIgnoreCase("ok")){
                    handler.post(new Runnable() {

                        public void run() {
                            campoMensagem.setText("");
                            Toast.makeText(MensagemActivity.this, "Mensagem enviada !", Toast.LENGTH_SHORT).show();
                        }

                    });
                }
                atualizar();

            }else if(acesso==CONSULTAR){
                Gson gson = new Gson();

                if(idInicial==0){
                    mensagens = gson.fromJson(retorno, new TypeToken<ArrayList<Mensagem>>() {}.getType());
                }else {
                    mensagens.addAll((ArrayList<Mensagem>) gson.fromJson(retorno, new TypeToken<ArrayList<Mensagem>>() {
                    }.getType()));
                }

                mensagemList=new ArrayList<HashMap<String, String>>();
                for(Mensagem mensagem: mensagens){

                    HashMap<String, String> men = new HashMap<String, String>();
                    men.put("id", ""+mensagem.getId());
                    men.put("nome", mensagem.getNomeCliente()+"-"+mensagem.getData()+":");
                    men.put("mensagem", mensagem.getMensagem());
                    mensagemList.add(men);
                    idInicial = mensagem.getId();

                }
                handler.post(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter( MensagemActivity.this,mensagemList, R.layout.view_mensagem_registro, new String[] { "id","nome","mensagem"}, new int[] {R.id.id, R.id.nome,R.id.mensagem});
                        setListAdapter(adapter);
                        ListView lv = getListView();
                        lv.setSelection(adapter.getCount() - 1);
                    }
                });


            }


        }
    }

    public void atualizar(){

        acesso=CONSULTAR;
        new Thread(MensagemActivity.this).start();

    }
}
