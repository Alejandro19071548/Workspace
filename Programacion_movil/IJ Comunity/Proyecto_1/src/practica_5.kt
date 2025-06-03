fun main() {
    print("Ingrese el sueldo del empleado:")
    val sueldo = readln().toDouble()
    if (sueldo > 3000) {
        println("Debe pagar impuestos")
    }else{
        println("No se debe pagar impuestos :D")
    }
}