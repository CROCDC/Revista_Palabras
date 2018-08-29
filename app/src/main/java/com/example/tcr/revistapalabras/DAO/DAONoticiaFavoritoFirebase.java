package com.example.tcr.revistapalabras.DAO;

import android.support.annotation.NonNull;

import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.Utils.Helper;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAONoticiaFavoritoFirebase {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    public DAONoticiaFavoritoFirebase() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Helper.REFERENCIA_CONTENIDO_FAVORITO).child(user.getUid());

    }

    public void pedirListaDeNoticiasFavoritas(final ResultListener<List<Noticia>> escuchadorDelControlador) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Noticia> listaDeNoticia = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Noticia noticia = snapshot.getValue(Noticia.class);

                    listaDeNoticia.add(noticia);
                }
                escuchadorDelControlador.finish(listaDeNoticia);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void verficiarSiLaNoticiaEstaEnFirebase(Noticia noticia, final ResultListener<Boolean> escuchadorDelControlador) {
        databaseReference = firebaseDatabase.getReference().child(Helper.REFERENCIA_CONTENIDO_FAVORITO).child(user.getUid()).child(noticia.getId().toString());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    escuchadorDelControlador.finish(false);
                }else{
                    escuchadorDelControlador.finish(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void agregarLaNoticiaAGuardado(Noticia noticia,final ResultListener<Boolean> escuchadorDelControlador){

    }


}
