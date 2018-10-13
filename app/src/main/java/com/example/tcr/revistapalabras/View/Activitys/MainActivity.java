package com.example.tcr.revistapalabras.View.Activitys;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tcr.revistapalabras.DAO.DAOApiPushNotification;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.R;
import com.example.tcr.revistapalabras.Utils.Helper;
import com.example.tcr.revistapalabras.View.Adapter.ViewPagerAdapterPorListaDeFragments;
import com.example.tcr.revistapalabras.View.Fragments.FragmentAgendaCultural;
import com.example.tcr.revistapalabras.View.Fragments.FragmentContenidoFavorito;
import com.example.tcr.revistapalabras.View.Fragments.FragmentUltimasNoticias;
import com.example.tcr.revistapalabras.View.Fragments.FragmentNotasPorCategoria;
import com.example.tcr.revistapalabras.View.Fragments.FragmentNoticiasBreves;
import com.example.tcr.revistapalabras.View.Fragments.FragmentResultadoDeLaBusqueda;
import com.example.tcr.revistapalabras.View.Fragments.I_NotificadorHaciaMainActivity;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements I_NotificadorHaciaMainActivity {

    private View view;


    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private TextView textViewSesiones;
    private ImageView imageViewFotoDePerfil;
    private TextView textViewNombreDelUsuario;

    private View header;

    private android.support.v7.widget.Toolbar toolbar;
    private ImageView imageViewMenuHamburguesa;
    private TextView textViewTituloRevistaPalabras;
    private ImageView imageViewButtonSearch;
    private EditText editTextCampoDeBusqueda;
    private InputMethodManager inputMethodManager;

    private ViewPagerAdapterPorListaDeFragments viewPagerAdapterPorListaDeFragments;
    private TabLayout tabLayoutPrincipal;
    private ViewPager viewPagerPrincipal;
    private CardView cardViewContenedorTabLayout;

    private Integer posicionAnterior;
    private Integer posicionActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            new DAOApiPushNotification().insertarToken();
        } catch (Exception e) {
            Log.d("Notificaciones", e.toString());
        }


        navigationView = findViewById(R.id.navigationViewCategorias_activitymain);
        drawerLayout = findViewById(R.id.contenedorActivityMain_activitymain);

        toolbar = findViewById(R.id.toolbarPrincipal_toolbar);
        imageViewMenuHamburguesa = findViewById(R.id.imageViewMenuAmburguesa_toolbar);
        imageViewButtonSearch = findViewById(R.id.imageViewButtonSearch_toolbar);
        editTextCampoDeBusqueda = findViewById(R.id.editTextBusqueda_toolbar);
        textViewTituloRevistaPalabras = findViewById(R.id.textviewTituloRevistaPalabras_toolbar);

        header = navigationView.getHeaderView(0);
        textViewSesiones = header.findViewById(R.id.buttonActionsAcounts);
        imageViewFotoDePerfil = header.findViewById(R.id.imageViewFotoDePerfil);
        textViewNombreDelUsuario = header.findViewById(R.id.textViewNombreDeUsuario);
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        tabLayoutPrincipal = findViewById(R.id.tabLayoutPrincipal_activitymain);
        viewPagerPrincipal = findViewById(R.id.viewPagerPrincipal_activitymain);
        cardViewContenedorTabLayout = findViewById(R.id.cardViewContenedorTabLayout_activitymain);


        viewPagerAdapterPorListaDeFragments = new ViewPagerAdapterPorListaDeFragments(getSupportFragmentManager());

        viewPagerAdapterPorListaDeFragments.addFragment(new FragmentUltimasNoticias(), "Inicio");
        viewPagerAdapterPorListaDeFragments.addFragment(new FragmentAgendaCultural(), "Agenda cultural");
        viewPagerAdapterPorListaDeFragments.addFragment(new FragmentNoticiasBreves(), "Noticias Breves");

        viewPagerPrincipal.setAdapter(viewPagerAdapterPorListaDeFragments);
        tabLayoutPrincipal.setupWithViewPager(viewPagerPrincipal);


        navigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);

        touchSerchButton();
        informacionScrollViewPager();

        imageViewMenuHamburguesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.START);
            }
        });


        textViewSesiones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    //SIGN OUT
                    FirebaseAuth.getInstance().signOut();
                    LoginManager.getInstance().logOut();

                    refreshNavView();
                } else {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }

        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshNavView();
    }

    @Override
    public void onBackPressed() {
        viewPagerPrincipal.setVisibility(View.VISIBLE);
        tabLayoutPrincipal.setVisibility(View.VISIBLE);
        cardViewContenedorTabLayout.setVisibility(View.VISIBLE);
        if (editTextCampoDeBusqueda.getVisibility() == View.VISIBLE) {
            editTextCampoDeBusqueda.setVisibility(View.INVISIBLE);
            textViewTituloRevistaPalabras.setVisibility(View.VISIBLE);
        } else if (navigationView.getVisibility() == View.VISIBLE) {
            drawerLayout.closeDrawer(Gravity.START);

        } else {
            try {
                if (posicionActual == 0) {
                    super.onBackPressed();
                } else {
                    viewPagerPrincipal.setCurrentItem(posicionAnterior);
                }
            } catch (Exception e) {
                super.onBackPressed();
            }
        }


    }

    private void refreshNavView() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            textViewSesiones.setText("Cerrar Sesion");
            Glide.with(MainActivity.this)
                    .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl())
                    .into(imageViewFotoDePerfil);
            textViewNombreDelUsuario.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());


        } else {
            textViewSesiones.setText("Iniciar Sesion");
            imageViewFotoDePerfil.setImageResource(R.drawable.acountnegro);
            textViewNombreDelUsuario.setText("Invitado");


        }

    }

    @Override
    public void notificar(Noticia unNoticia) {


        Intent intent = new Intent(MainActivity.this, DescripcionesDeNotasActivity.class);

        Bundle bundle = new Bundle();


        bundle.putSerializable(DescripcionesDeNotasActivity.CLAVE_OBJETO_CONTENIDO, unNoticia);

        intent.putExtras(bundle);


        startActivity(intent);

        overridePendingTransition(R.anim.entrada_por_arriba_activity, R.anim.salida_por_abajo);

    }

    @Override
    public void notificarTouchPublicidad(String link) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(link));
        startActivity(i);
    }

    @Override
    public void notificarTouchRedSocial(Integer numero) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        switch (numero) {
            case Helper.REFERENCIA_TWITTER:
                i.setData(Uri.parse("https://twitter.com/PalabrasRevist"));
                startActivity(i);
                break;
            case Helper.REFERENCIA_FACEBOOK:
                i.setData(Uri.parse("https://www.facebook.com/www.palabras.la"));
                startActivity(i);
                break;

            case Helper.REFERENCIA_INSTAGRAM:
                i.setData(Uri.parse("https://www.instagram.com/revista_palabras/"));
                startActivity(i);
                break;
        }
    }


    NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.opcionFavoritos:
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        FragmentContenidoFavorito fragmentContenidoFavorito = FragmentContenidoFavorito.fabricaDeFragmentContenidoFavorito();
                        cargarFragment(fragmentContenidoFavorito);
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }

                    break;
                case R.id.opcionAmerica:
                    cargarFragmentPorCategoria(Helper.AMERICA, getString(R.string.america));
                    break;
                case R.id.opcionArquitectura:
                    cargarFragmentPorCategoria(Helper.ARQUITECTURA, getString(R.string.arquitectura));
                    break;
                case R.id.opcionArtesEscenicas:
                    cargarFragmentPorCategoria(Helper.ARTESESCENICAS, getString(R.string.artes_esc_nicas));
                    break;
                case R.id.opcionAudioVisuales:
                    cargarFragmentPorCategoria(Helper.AUDIOVISUALES, getString(R.string.audiovisuales));
                    break;
                case R.id.opcionBalancesyPerspectivas:
                    cargarFragmentPorCategoria(Helper.BALANCESYPERSPECTIVAS, getString(R.string.balances_y_perspectivas));
                    break;
                case R.id.opcionBallet:
                    cargarFragmentPorCategoria(Helper.BALLET, getString(R.string.ballet));
                    break;
                case R.id.opcionCineySeries:
                    cargarFragmentPorCategoria(Helper.CINEYSERIES, getString(R.string.cine_y_series));
                    break;
                case R.id.opcionLiteratura:
                    cargarFragmentPorCategoria(Helper.LITERATURA, getString(R.string.literatura));
                    break;
                case R.id.opcionFotografía:
                    cargarFragmentPorCategoria(Helper.FOTOGRAFIAS, getString(R.string.fotografía));
                    break;
                case R.id.opcionIdeas:
                    cargarFragmentPorCategoria(Helper.IDEAS, getString(R.string.ideas));
                    break;
            }
            drawerLayout.closeDrawers();
            return false;
        }
    };


    //METODOS DE CARGA DE FRAGMENTS

    public void cargarFragmentPorLugar(Fragment fragment, Integer lugarARemplazar) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(lugarARemplazar, fragment);
        fragmentTransaction.setCustomAnimations(R.anim.entrada_por_la_derecha, R.anim.salida_por_la_derecha, R.anim.entrada_por_la_derecha, R.anim.salida_por_la_derecha);
        fragmentTransaction.commit();

    }

    public void cargarFragmentPorCategoria(Integer categoria, String nombreDeLaCategoria) {
        FragmentNotasPorCategoria fragmentNotasPorCategoria = FragmentNotasPorCategoria.fabricaDeFragmentPorCategoria(categoria, nombreDeLaCategoria);

        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearLayoutContenedorDeFragments_activitymain, fragmentNotasPorCategoria).addToBackStack(null);
        fragmentTransaction.setCustomAnimations(R.anim.entrada_por_la_derecha, R.anim.salida_por_la_derecha, R.anim.entrada_por_la_derecha, R.anim.salida_por_la_derecha);
        fragmentTransaction.commit();
        viewPagerPrincipal.setVisibility(View.GONE);
        tabLayoutPrincipal.setVisibility(View.GONE);
        cardViewContenedorTabLayout.setVisibility(View.GONE);


    }

    public void cargarFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.entrada_por_la_derecha, R.anim.salida_por_la_derecha);
        fragmentTransaction.replace(R.id.linearLayoutContenedorDeFragments_activitymain, fragment).addToBackStack(null);
        fragmentTransaction.commit();
        viewPagerPrincipal.setVisibility(View.GONE);
        tabLayoutPrincipal.setVisibility(View.GONE);
        cardViewContenedorTabLayout.setVisibility(View.GONE);


    }


    public void touchSerchButton() {
        imageViewButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editTextCampoDeBusqueda.setVisibility(View.VISIBLE);
                textViewTituloRevistaPalabras.setVisibility(View.GONE);
                editTextCampoDeBusqueda.requestFocus();
                editTextCampoDeBusqueda.requestFocus(View.FOCUS_DOWN);
                editTextCampoDeBusqueda.setFocusableInTouchMode(true);

                inputMethodManager.showSoftInput(editTextCampoDeBusqueda, InputMethodManager.SHOW_IMPLICIT);
                if (!(editTextCampoDeBusqueda.getText().toString().equals(""))) {
                    cargarFragment(FragmentResultadoDeLaBusqueda.fabricaDeFragmentsResultadoDeLaBusqueda(editTextCampoDeBusqueda.getText().toString()));

                    textViewTituloRevistaPalabras.setVisibility(View.VISIBLE);
                    editTextCampoDeBusqueda.setVisibility(View.INVISIBLE);
                    editTextCampoDeBusqueda.setText("");
                }
                editTextCampoDeBusqueda.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (editTextCampoDeBusqueda.getText().toString().equals("")) {

                        }
                        if (!(editTextCampoDeBusqueda.getText().toString().equals(""))) {
                            cargarFragment(FragmentResultadoDeLaBusqueda.fabricaDeFragmentsResultadoDeLaBusqueda(editTextCampoDeBusqueda.getText().toString()));

                            textViewTituloRevistaPalabras.setVisibility(View.VISIBLE);
                            editTextCampoDeBusqueda.setVisibility(View.INVISIBLE);
                            editTextCampoDeBusqueda.setText("");
                        }
                    }
                });
            }
        });

        editTextCampoDeBusqueda.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (!(editTextCampoDeBusqueda.getText().toString().equals(""))) {
                    cargarFragment(FragmentResultadoDeLaBusqueda.fabricaDeFragmentsResultadoDeLaBusqueda(editTextCampoDeBusqueda.getText().toString()));

                    textViewTituloRevistaPalabras.setVisibility(View.VISIBLE);
                    editTextCampoDeBusqueda.setVisibility(View.GONE);
                    editTextCampoDeBusqueda.setText("");
                }
                return false;
            }
        });

    }

    public void informacionScrollViewPager() {

        viewPagerPrincipal.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                posicionAnterior = viewPagerPrincipal.getCurrentItem() - 1;
                posicionActual = position;


            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void loginExitoso() {
        FancyToast.makeText(this, "Login exitoso", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();

    }

    public void loginFallido() {
        FancyToast.makeText(this, "hubo un error en el login", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

    }

}
