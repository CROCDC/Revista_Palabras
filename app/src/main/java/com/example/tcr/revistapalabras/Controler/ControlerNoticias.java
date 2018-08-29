package com.example.tcr.revistapalabras.Controler;

import com.example.tcr.revistapalabras.DAO.DAONoticiaRetrofit;
import com.example.tcr.revistapalabras.Model.Noticia;
import com.example.tcr.revistapalabras.Utils.Helper;
import com.example.tcr.revistapalabras.Utils.ResultListener;

import java.util.List;

public class ControlerNoticias {

    public static final int PaginaInicial = 1;
    public static final int PaginasTotales = 10;
    public static final Boolean paginar = true;
    private Integer paginaActual;


    public ControlerNoticias() {
        paginaActual = 1;
    }

    public void pedirListaDeLasUltimasNoticiasPublicadas(final ResultListener<List<Noticia>> escuchadorDeLaVista) {
        new DAONoticiaRetrofit().pedirListaDeLasUltimasNoticiasPublicadas(new ResultListener<List<Noticia>>() {

            @Override
            public void finish(final List<Noticia> resultado) {

                Helper.acomodarPreviewDeNotasPorLista(resultado);

                Helper.eliminarNoticiasBrevesYAgenda(resultado);

                escuchadorDeLaVista.finish(resultado);

                setPaginaActual(paginaActual += 1);

            }
        }, paginaActual);
    }

    public void pedirListaDeNoticiassDeLaAgenda(Integer pagina, Integer tamaño, final ResultListener<List<Noticia>> escuchadorDeLaVista) {
        new DAONoticiaRetrofit().pedirListaDeNoticiasDeLaAgenda(pagina, tamaño, new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {
                escuchadorDeLaVista.finish(resultado);
            }
        });
    }

    public void pedirListaDeNoticiasPorCategoria(final ResultListener<List<Noticia>> escuchadorDeLaVista, Integer categoria) {
        new DAONoticiaRetrofit().pedirListaDeNotasPorCategoria(new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {
                if (!(resultado == null)) {
                    Helper.acomodarPreviewDeNotasPorLista(resultado);
                    escuchadorDeLaVista.finish(resultado);
                    setPaginaActual(paginaActual += 1);
                }

            }
        }, paginaActual, categoria);
    }

    public void pedirListaDeNoticiasDeLaBusqueda(String busqueda, final ResultListener<List<Noticia>> escuchadorDeLaVista) {
        new DAONoticiaRetrofit().obtenerBusqueda(busqueda, new ResultListener<List<Noticia>>() {
            @Override
            public void finish(List<Noticia> resultado) {
                Helper.acomodarPreviewDeNotasPorLista(resultado);
                escuchadorDeLaVista.finish(resultado);
            }
        });

    }

    public void pedirNoticiasPorID(Integer id, final ResultListener<Noticia> escuchadorDeLaVista) {
        new DAONoticiaRetrofit().obtenerNotaPorID(id, new ResultListener<Noticia>() {
            @Override
            public void finish(Noticia resultado) {
                escuchadorDeLaVista.finish(resultado);
            }
        });
    }

    public void setPaginaActual(Integer paginaActual) {
        this.paginaActual = paginaActual;
    }
}
