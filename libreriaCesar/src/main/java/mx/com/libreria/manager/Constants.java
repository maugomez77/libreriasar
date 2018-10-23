package mx.com.libreria.manager;

/**
 * Interface para definir valores constantes usados por las
 * utilerias y realizar ciertas validaciones.
 */
public interface Constants {

	String BREAK_LINE_HTML = "<br/>";
	
	String BREAK_NEW_LINE_HTML = "\\n";
	
	String DATOS_FALTANTES = "Faltan los siguientes datos por completar: " + BREAK_NEW_LINE_HTML;
	
	String DELIMITADOR_PUNTO = ".";
	String DELIMITADOR_COMA = ",";
	
	String ROL_ADMON = "1";
	
	String YES_VALUE = "Y";
	String YES_DESC = "SI";
	
	String NO_VALUE = "N";
	String NO_DESC = "NO";
	
	String YES_NO_KEY = "Y/N";
	
	String PAGO_CREDITO = "C";
	String DESC_PAGO_CREDITO = "Credito";
	
	String PAGO_CONTADO = "P";
	String DESC_PAGO_CONTADO = "Contado";
	
	String PAGO_INVENTARIO = "I";
	String DESC_PAGO_INVENTARIO = "Carga de Inventario";
	
	int INFINITY_VALUE_INTEGER = 1000000;
	
	float INFINITY_VALUE_FLOAT = 999999999;
	
	String INDEX_PAGE = "index.jsp";
	
	String LOGIN_REGISTER_NEW = "/login/Register.jsp";
	
	String LOGIN_INDEX = "/login/index.jsp";
	
	String OBJETO_VENTA = "objetoVenta";
	
	String OBJETO_COMPRA = "objetoCompra";
	
	String COMPRA_PROVEEDOR = "compraProveedor";
	String NUMERO_PROVEEDOR = "numeroProveedor";
	
	String COMPRA_CLIENTE = "compraCliente";
	String COMPRA_EMPLEADO = "compraEmpleado";
	
	String REMISION_ACTUAL_SESION = "remisionActualSesion";
	
	String INVENTARIO_ACTUAL_SESION = "inventarioActualSesion";
	
	String ESTATUS_PAGO_COMPRA = "estatusPagoCompra";
	
	String ESTATUS_PAGO_VENTA = "estatusPagoVenta";
	
	String DECIMAL_FORMAT = "########.##";
	
	String START_DATE_RANGE = " 00:00:00";
	
	String END_DATE_RANGE = " 23:59:59";	
	
	String FORMAT_DATE = "yyyy-MM-dd";
	
	String FORMAT_DATE_WITH_HOUR = "yyyy-MM-dd HH:mm:ss";
	
	String PROVEEDOR_ID_INVENTARIO = "proveedorIdInventario";
	String REQUEST_PROVEEDOR_SESION = "proveedorIdInventario";
	
	String CLIENTE_ID_INVENTARIO = "clienteIdInventario";
	String EMPLEADO_ID_INVENTARIO = "empleadoId";
	
	int NUMERO_MESES_ANIO = 12;
	int NUMERO_MESES_SEMESTRAL = 6;
	
	String TIPO_PRODUCTO_NORMAL_VALUE = "NOR";
	String TIPO_PRODUCTO_NORMAL_DESC = "Normal";
	
	String TIPO_PRODUCTO_VENTA_ESPECIAL_VALUE = "VE";
	String TIPO_PRODUCTO_VENTA_ESPECIAL_DESC = "Venta Especial";
	
	String TIPO_PRODUCTO_SUSCRIPCION_VALUE = "SUS";
	String TIPO_PRODUCTO_SUSCRIPCION_DESC = "Suscripcion";
	
	String TIPO_PRODUCTO_KEY = "TIPO_PRODUCTO_KEY";
	
	String STRING_VACIO = "";
	
	String ENTREGA_KEY = "M/B";
	
	String MENSUAL_VALUE = "M";
	String MENSUAL_DESC = "Mensual";
	
	String BIMENSUAL_VALUE = "B";
	String BIMENSUAL_DESC = "Bimensual";
	
	String COMPRAS_VALUE = "C";
	String COMPRAS_DESC = "Compras";
	
	String VENTAS_VALUE = "V";
	String VENTAS_DESC = "Ventas";
	
	String SUSCRIPCION_VALUE = "S";
	String SUSCRIPCION_DESC = "Suscripciones";
	
	String VENTAS_ESPECIALES_VALUE = "VE";
	String VENTAS_ESPECIALES_DESC = "Ventas Especiales";

	String GASTO_VALUE = "GA";
	String GASTO_DESC = "Gastos Aplicados";
	
	float IVA_PORCENTAJE = 0.16f;
	
	String IVA_DESC = "IVA 16 %";
}
