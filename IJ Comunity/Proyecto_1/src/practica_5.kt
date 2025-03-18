fun main(parametro: Array<String>) {
    print("Ingrese el sueldo del empleado:")
    val sueldo = readLine()!!.toDouble()
    if (sueldo > 3000) {
        println("Debe pagar impuestos")
    }else{
        println("No se debe pagar impuestos :D")
    }
}