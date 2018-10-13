package com.example.tcr.revistapalabras.DAO;

import android.support.annotation.NonNull;

import com.example.tcr.revistapalabras.Model.Publicidad;
import com.example.tcr.revistapalabras.Utils.Helper;
import com.example.tcr.revistapalabras.Utils.ResultListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DAOPublicidadesFirebase {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    public DAOPublicidadesFirebase(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child(Helper.REFERENCIA_PUBLICIDADES);

    }


    public void traerListaDePublicidades(final ResultListener<List<Publicidad>> escuchadorDelControler){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Publicidad> listaDePublicidades = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Publicidad publicidad = snapshot.getValue(Publicidad.class);

                    listaDePublicidades.add(publicidad);
                }

                escuchadorDelControler.finish(listaDePublicidades);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }
}
