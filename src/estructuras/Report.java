package estructuras;

import beans.Cliente;
import beans.Conductor;
import java.io.File;

/**
 *
 * @author pedro
 */
public class Report {

    public Report() {
    }

    /*metodo que grafica la matriz dispersa este metodo 
    * es privado puesto que es llamando por el metodo publico
    * pide como parametro el vector y un StringBuilder donde
    * se va concatenando el codigo dot para generar la grafica
     */
    private void generate_source_dot(StringBuilder dotsource, NodoHash[] array) {
        dotsource.append("nodel[label=\"Vector");
        StringBuilder nodes = new StringBuilder(); //string para nodos
        StringBuilder dir = new StringBuilder(); //string para direccion a que nodos debe apuntar
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                // System.out.print("[" + i + "] -> ");//encabezado
                dotsource.append("|<f").append(i).append(">").append(i);//vector posiciones llenas
                dir.append("nodel:f").append(i).append(" -> ");
                DoubleLinkedList<Cliente> tmp = array[i].getList_client();
                nodes.append("node").append(i).append("[label=\"{<g> |");
                for (int j = 0; j < tmp.getSize(); j++) {
                    Cliente temp = tmp.getData();
                    nodes.append("Nombre: ").append(temp.getNombres()).append("\\n").append("DPI: ").append(temp.getDpi()).append("\\n").append("Telefono: ").append(temp.getTelefono()).append("|");
                }
                nodes.append("<s>}\"]\n");
                dir.append("node").append(i).append(":g[arrowtail=dot, dir=both,tailclip=false];\n");
            } else {
                dotsource.append("|<f").append(i).append(">").append(i);
            }
        }
        dotsource.append("\", height=.5];\n");
        dotsource.append(nodes);
        dotsource.append(dir);
    }

    //metodo publico para generar imagen pide como parametro el vector
    public String reportHashTable(NodoHash[] array) {
        StringBuilder dotsource = new StringBuilder();
        Graphviz graph = new Graphviz();
        graph.addln(graph.start_graph());
        graph.addln("rankdir=LR;");
        graph.addln("node [shape=record, color=blue, height=.1, width=.1];");
        graph.addln("edge[color=red];");
        graph.addln("graph [nodesep=0.5];");
        generate_source_dot(dotsource, array);
        graph.add(dotsource.toString());
        graph.add(graph.end_graph());
        File f = new File("Hashtable.png");
        graph.writeGraphToFile(graph.getGraph(graph.getDotSource(), "png"), f);
        return graph.getPath();
    }

    //metod publico para genera reporte de grafos pide como parameto el nodo cabeza del grafo
    //y deuvuelve el string de la ruta donde se creo el archivo
    public String resporteGrafos(NodoG inicio) {
        StringBuilder dotsource = new StringBuilder();
        Graphviz graph = new Graphviz();
        graph.addln(graph.start_graph());
        graph.addln("rankdir=LR;");
        graph.addln("node [shape=plaintext];");
        graph.addln("edge[color=blue];");
        get_dot_grafos(dotsource, inicio);
        graph.add(dotsource.toString());
        graph.add(graph.end_graph());
        File f = new File("GrafosRutas.png");
        graph.writeGraphToFile(graph.getGraph(graph.getDotSource(), "png"), f);
        return graph.getPath();
    }

    //metodo donde se genera el string de dot para los grafos
    private void get_dot_grafos(StringBuilder dotsorce, NodoG inicio) {
        StringBuilder auxs1 = new StringBuilder(), auxs2 = new StringBuilder();
        NodoG naux = inicio;
        ArcoG aaux;
        while (naux != null) {
            auxs1.append(naux.nombre).append("[label=\"").append(naux.getNombre()).append("\"];\n");
            aaux = naux.adyacencia;
            while (aaux != null) {
                //System.out.println("-"+aaux.peso+"-> "+aaux.adyacencia.nombre);
                auxs2.append(naux.getNombre()).append(" -> ").append(aaux.adyacencia.nombre).append("[label=\"").append(aaux.peso).append("\"];\n");
                aaux = aaux.siguiente;
            }
            naux = naux.siguiente;
        }
        dotsorce.append(auxs1).append(auxs2);
    }

    //metodo que grafica una lista Simple recibe como parametro la lista
    public static String reporteListaSimple(DoubleLinkedList<Conductor> list, String nombre) {
        Graphviz graph = new Graphviz();
        graph.addln(graph.start_graph());
        graph.addln("rankdir=LR;");
        graph.addln("node [shape=record, color=blue]; ");
        graph.addln();
        int contador = 0;
        String nodos = "", enlaces = "";
        Node<Conductor> temp = list.getHead();
        int size = list.getSize();
        if (temp == null) {
            nodos = "La Lista está vacía ";
        } else {
            do {
                if (contador < size - 1) {
                    nodos = nodos + "node" + contador + " [label=\"{DPI: " + temp.getData().getDpi() + " \\nNombre: " + temp.getData().getNombres() + "|<b>}\"];\n";
                    enlaces = enlaces + "node" + contador + ":b:c -> node" + (contador + 1) + ":c [arrowtail=dot, dir=both,tailclip=false];\n";
                } else {
                    nodos = nodos + "node" + contador + " [label=\"{DPI: " + temp.getData().getDpi() + " \\nNombre: " + temp.getData().getNombres() + "|<b>}\"];\n";
                    nodos = nodos + "node" + (contador + 1) + " [shape=point];\n";
                    enlaces = enlaces + "node" + contador + ":b:c -> node" + (contador + 1) + ":c [arrowtail=dot, dir=both,tailclip=false];\n";
                }
                contador++;
                temp = temp.next;
            } while (temp != list.getHead());
        }
        graph.addln(nodos);
        graph.addln(enlaces);
        graph.addln(graph.end_graph());
        File out = new File(nombre + ".png");
        graph.writeGraphToFile(graph.getGraph(graph.getDotSource(), "png"), out);
        return graph.getPath();
    }

    //metodo para el reporte de una lista circular recibe como parametro la lista y el nombre para la grafica
    public static String reportListaDobleCircular(DoubleLinkedList<Conductor> ldc, String nombre) {
        Graphviz graph = new Graphviz();
        graph.addln(graph.start_graph());
        graph.addln("rankdir=LR;");
        graph.addln("node [shape=record, color=blue, width=0.5, height=0.5]; ");
        graph.addln();
        int contador = 0, size = ldc.getSize();
        Node<Conductor> primero = ldc.getHead();
        /*StringBuilder*/
        String nodos = "", enlaces = "", enlacesIverso = "";
        if (ldc != null && size > 0) {
            Node<Conductor> aux = primero;
            do {
                if (contador < size - 1) {
                    nodos = nodos + "node" + contador + "[label=\"{<a>|DPI: " + aux.getData().getDpi() + "\\nNombre: " + aux.getData().getNombres() + "|<b>}\"];\n";
                    enlaces = enlaces + "node" + contador + " -> node" + (contador + 1) + ";\n";
                    enlacesIverso = enlacesIverso + "node" + (contador + 1) + " -> node" + contador + ";\n";
                } else {
                    nodos = nodos + "node" + contador + "[label=\"{<a>|DPI: " + aux.getData().getDpi() + "\\nNombre: " + aux.getData().getNombres() + "|<b>}\"];\n";
                    enlaces = enlaces + "node" + (contador) + ":b:c -> node" + (0) + ":a:c [arrowtail=dot, dir=both,tailclip=false];\n";
                    enlacesIverso = enlacesIverso + "node" + (0) + ":a:c -> node" + (contador) + ":b:c [arrowtail=dot, dir=both,tailclip=false];\n";
                }
                contador++;
                aux = aux.next;
            } while (aux != primero);
        }
        graph.addln(nodos);
        graph.addln(enlaces);
        graph.addln(enlacesIverso);
        graph.addln(graph.end_graph());
        File out = new File(nombre + ".png");
        graph.writeGraphToFile(graph.getGraph(graph.getDotSource(), "png"), out);
        return graph.getPath();
    }

    //metodo que gener el reporte para block chain pide como parametro la lista doblemente enlazada simple
    public static String reporteBlockChain(DoubleLinkedList<Block> blockchain) {
        Graphviz graph = new Graphviz();
        graph.addln(graph.start_graph());
        graph.addln("rankdir=LR;");
        graph.addln("node [fontsize=\"16\", shape=\"ellipse\"]; ");
        graph.addln();
        int contador = 0;
        String nodos = "", enlaces = "";
        Node<Block> temp = blockchain.getHead();
        int size = blockchain.getSize();
        if (temp == null) {
            nodos = "La Lista está vacía ";
        } else {
            while (temp != null) {
                if (contador < size - 1) {
                    nodos = nodos + "node" + contador + " [label=\"<f0>Pre H(" + temp.getData().getPreovioushash() + ")|<f1>HASH: "+temp.getData().getHash()+"\\nFecha: " + temp.getData().getViaje().getFecha_hora() + " \\nPlaca: " + temp.getData().getViaje().getVehiculo().getPlaca() + "\", shape=\"record\"];\n";
                    enlaces = enlaces + "node" + contador + ":f0 -> node" + (contador + 1) + ":f0;\n";
                } else {
                    nodos = nodos + "node" + contador + " [label=\"<f0>Pre H(" + temp.getData().getPreovioushash() + ")|<f1>HASH: "+temp.getData().getHash()+"\\nFecha: " + temp.getData().getViaje().getFecha_hora() + " \\nPlaca: " + temp.getData().getViaje().getVehiculo().getPlaca() + "\", shape=\"record\"];\n";
                    nodos = nodos + "node" + (contador + 1) + " [shape=point];\n";
                    enlaces = enlaces + "node" + contador + ":f0 -> node" + (contador + 1) + ":f0 [arrowtail=dot, dir=both,tailclip=false];\n";
                }
                contador++;
                temp = temp.next;
            }
        }
        graph.addln(nodos);
        graph.addln(enlaces);
        graph.addln(graph.end_graph());
        File out = new File("BlockChain" + ".png");
        graph.writeGraphToFile(graph.getGraph(graph.getDotSource(), "png"), out);
        return graph.getPath();
    }

}
