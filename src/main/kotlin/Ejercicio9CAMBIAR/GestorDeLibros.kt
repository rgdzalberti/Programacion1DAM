package Ejercicio9CAMBIAR

import java.util.logging.Level
import java.util.logging.LogManager

internal val l = LogManager.getLogManager().getLogger("").apply { level = Level.ALL }
internal fun i(tag: String, msg: String) {
    l.info("[$tag] - $msg")
}

interface GestorCatalogo {
    fun existeLibro(idLibro: String): Boolean
    fun infoLibro(idLibro: String): Map<String, Any>
}


fun main() {
    var portatil = "/home/edu/IdeaProjects/IESRA-DAM-Prog/ejercicios/src/main/kotlin/un5/eje5_4/Catalog.xml"
    //var casa = "/home/usuario/Documentos/workspace/IdeaProjects/IESRA-DAM/ejercicios/src/main/kotlin/un5/eje5_4/Catalog.xml"

    val gestorDeLibros = gestionLibros(CatalogoLibrosXML("C:\\Users\\usuarioT\\IdeaProjects\\Programacion-T6\\src\\main\\kotlin\\DAM1_6_5_RGD\\catalogo"))
    gestorDeLibros.preguntarPorUnLibro()
    gestorDeLibros.mostrarInfoDeUnLibro()

}

class gestionLibrosIUT1(){

    //Los parámetros idLibro e infoLibro se introducirán solamente si es necesario en el print
    fun ImprimirEnPantalla(caso: Int, idLibro: String = "", infoLibro: Map<String, Any> = emptyMap()){
        when (caso){
            1 -> println("Introduzca un ID: ")
            2 -> println("¡El libro $idLibro existe!")
            3 -> println("¡El libro $idLibro NO existe!")
            4 -> println("La información sobre es la siguiente\n$infoLibro")
            5 -> println("No se encontró información sobre el libro")
        }
    }
    fun Input(): String{var input = readln(); return input}

}

class gestionLibrosIUT2(){

    //Parameters idLibro and infoLibro will only be introduced if necessary for printing
    fun ImprimirEnPantalla(caso: Int, idLibro: String = "", infoLibro: Map<String, Any> = emptyMap()){
        when (caso){
            1 -> println("Enter an ID: ")
            2 -> println("Book $idLibro exists!")
            3 -> println("¡Book $idLibro does NOT exist!")
            4 -> println("The information about it is the next\n$infoLibro")
            5 -> println("Information about the book wasn't found")
        }
    }
    fun Input(): String{var input = readln(); return input}

}

class gestionLibros(catalogo: GestorCatalogo)
{
    var cat: GestorCatalogo = catalogo
    var IUT1 = gestionLibrosIUT1()

    fun preguntarPorUnLibro() {
        IUT1.ImprimirEnPantalla(1)
        var idLibro = IUT1.Input()
        if (cat.existeLibro(idLibro))
            IUT1.ImprimirEnPantalla(2, idLibro)
        else
            IUT1.ImprimirEnPantalla(3, idLibro)
    }

    fun mostrarInfoDeUnLibro()
    {
        IUT1.ImprimirEnPantalla(1)
        var idLibro = IUT1.Input()
        var infoLibro = cat.infoLibro(idLibro)
        if (!infoLibro.isEmpty())
            IUT1.ImprimirEnPantalla(4, "" ,infoLibro)
        else
            IUT1.ImprimirEnPantalla(5)
    }

}