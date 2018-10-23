package mx.com.libreria.manager;

import java.io.UnsupportedEncodingException;

import java.math.BigInteger;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import mx.com.libreria.model.Abono;
import mx.com.libreria.model.InventarioActual;
import mx.com.libreria.model.InventarioHistorico;
import mx.com.libreria.model.TipoGastoEnum;

public class Utilerias implements Constants {
	private static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
	//private static NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("ES"));
	private static DecimalFormat formateador = new DecimalFormat(DECIMAL_FORMAT);
	
	public static String imprimePaginaHTML(String cadena) { 
		return replaceCharacters(cadena, BREAK_NEW_LINE_HTML, BREAK_LINE_HTML);
	}
	
	public static Object getEnumValue(int opcionEnum, int request) {
		if (request > -1) { 
			if (opcionEnum == 1) {				
				if (request == TipoGastoEnum.Administrativos.getId()) { 
					return TipoGastoEnum.Administrativos;
				} else if (request == TipoGastoEnum.Venta.getId()) { 
					return TipoGastoEnum.Venta;
				} else if (request == TipoGastoEnum.Indirectos.getId()) { 
					return TipoGastoEnum.Indirectos;
				} else if (request == TipoGastoEnum.OtrosGastos.getId()) {
					return TipoGastoEnum.OtrosGastos;
				} else { 
					Logs.debug(Utilerias.class, "No hubo mapping en getEnumValue");
					return null;
				}
			}
		}
		return null;
	}
	/*
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Deprecated
	public static Collection convertSetList(Collection obj) {
	    if (obj instanceof java.util.List) {
	         return new HashSet((List)obj);
	    } else if(obj instanceof java.util.Set) {f
	         return new ArrayList((Set)obj);
	    }    
	    return null;
	}*/
	
	@SuppressWarnings("unchecked")
	public static Set<?> convertListToSet(List<?> elementos, Object obj) {		
		if (obj instanceof InventarioActual) { 
			HashSet<InventarioActual> conjunto = new HashSet<InventarioActual>((List<InventarioActual>)elementos);	
			return conjunto;
		} else if (obj instanceof InventarioHistorico) { 
			HashSet<InventarioHistorico> conjunto = new HashSet<InventarioHistorico>((List<InventarioHistorico>)elementos);	
			return conjunto;
		} else if (obj instanceof Abono) { 
			HashSet<Abono> conjunto = new HashSet<Abono>((List<Abono>)elementos);	
			return conjunto;
		} else { 
			Logs.debug(Utilerias.class, "No hubo conversion en convertListToSet ");
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<?> convertSetToList(Set<?> elementos, Object obj) {
		if (obj instanceof InventarioActual) {
			List<InventarioActual> conjunto = new ArrayList<InventarioActual>((Set<InventarioActual>)elementos);	
			return conjunto;
		} else if (obj instanceof Abono) {
			List<Abono> conjunto = new ArrayList<Abono>((Set<Abono>)elementos);	
			return conjunto;
		} else { 
			Logs.debug(Utilerias.class, "No hubo conversion en convertSetToList ");
			return null;
		}
	}
	
	public static String mostrarCamposFront(String str) { 
		if (str == null || str.equals("")) { 
			return "";
		}
		return str;
	}
	
	public static String mostrarCamposFront(int str) { 
		if (str < 0) { 
			return "";
		}
		return str + "";
	}
	
	public static String mostrarCamposFront(float str) { 
		if (str < 0) { 
			return "";
		}
		return str + "";
	}
	
	public static boolean isNullOrUndefined(Object obj) { 
		if (obj == null) { 
			return true;
		}
		return false;
	}
	
	public static boolean isNullOrUndefined(String str) { 
		if (str == null || str.equals("")) { 
			return true;
		}
		return false;
	}
	
	public static boolean isNullOrUndefined(int str) { 
		if (str < 0) { 
			return true;
		}
		return false;
	}
	
	public static boolean isNullOrUndefined(float str) { 
		if (str < 0) { 
			return true;
		}
		return false;
	}
	    
    public static String replaceComma(String str) {
		/****  hola, hola2, hola3 ****/	
		str = str.replace(",", "\",\"");
		return "\"" + str + "\"";
				
	}
        
    /**
    * Es o no aceptado un texto de acuerdo a un arreglo de caracteres posibles.
    * Si se recibe un valor nulo o un arreglo vacio en los argumentos,
    * regresar false como resultado.
    * @param  stringValue  Texto a comparar contra un arreglo de caracteres.
    * @param  charValues   Arreglo de tipo char.
    * @return boolean      True si el texto solo contiene caracteres que estan
    *                      contenidos dentro del arreglo de caracteres posibles,
    *                      false en caso contrario.
    */
    public static boolean isStringAccept(final String stringValue,
        final char[] charValues) {
        if ((stringValue != null) && (stringValue.length() != 0) 
            && (charValues != null) && (charValues.length != 0)) {
            for (int cont1 = 0; cont1 < stringValue.length(); cont1++) {
                if (!isCharacterAccept(stringValue.charAt(cont1), charValues)) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
    * Es o no aceptado un caracter de acuerdo a un arreglo de 
    * caracteres posibles.
    * Si se recibe un valor nulo o un arreglo vacio en los argumentos,
    * regresar false como resultado.
    * @param  charValue    Char a comparar contra un arreglo de 
    *                      caracteres posibles.
    * @param  charValues   Arreglo de tipo char.
    * @return boolean      True si el caracter esta contenido en el arreglo,
    *                      false en caso contrario.
    */
    public static boolean isCharacterAccept(final char charValue, 
            final char[] charValues) {
        if ((charValues != null) && (charValues.length != 0)) {
            for (int cont1 = 0; cont1 < charValues.length; cont1++) {
                if (charValue == charValues[cont1]) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Es o no aceptado un texto de acuerdo a un arreglo de objetos
     * String permitidos. Si se recibe un valor nulo o un arreglo
     * que contiene algun elemento nulo en los argumentos,
     * regresar false como resultado.
     * @param  stringValue   Texto a comparar contra un arreglo
     *                       de objetos String.
     * @param  stringValues  Arreglo de objetos String.
     * @return boolean       true si el texto esta contenido en el arreglo,
     *                       false en caso contrario.
     */
    public static boolean isStringAccept(final String stringValue,
        final String[] stringValues) {
        if ((stringValue != null) && !containsNullValues(stringValues)) {
            for (int cont1 = 0; cont1 < stringValues.length; cont1++) {
                if (stringValue.equals(stringValues[cont1])) {
                    return true;
                }
            }
        }

        return false;
    }   

    /**
    * Es o no un arreglo de objetos String que contiene algun elemento nulo.
    * Si se recibe un valor nulo o vacio, regresar true como resultado.
    * @param  stringValues   Arreglo de objetos String.
    * @return true           Si el arreglo de objetos String
    *                        contiene algun elemento nulo,
    *                        false en caso contrario.
    */
    public static boolean containsNullValues(final String[] stringValues) {
        if ((stringValues == null) || (stringValues.length == 0)) {
            return true;
        }

        for (int cont1 = 0; cont1 < stringValues.length; cont1++) {
            if (stringValues[cont1] == null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Obtener un long asociado al texto.
     * Si no es posible obtener un valor con el argumento,
     * regresar cero como resultado.
     * @param  stringValue   Texto a convertir en tipo long.
     * @return long          asociado al texto.
     */
    public static long getLongValue(final String stringValue) {
        try {
            return (new BigInteger(stringValue)).longValue();
        } catch (Exception ex) {
            return 0;
        }
    }

    /**
     * Metodo para checar si dos String contienen el mismo contenido.
     * @param  oldStr    Primer String
     * @param  newStr    Segundo String
     * @return boolean   True si son iguales, false de otra forma
     */
    public static boolean equalsString(final String oldStr, final String newStr) {
        return oldStr.equals(newStr);
    }

    /**
     * Metodo para regresar un String a partir de un double.
     * @param  val   Valor
     * @return String
     */
    public static String doubleToStr(final double val) {
        return String.valueOf(val);
    }
    
    /**
     * Metodo para convertir un String a entero.
     * @param  str    Strintr
     * @return double
     */    
    public static double strToDouble(final String str) { 
        return strToDouble(str, -1);
    }
    /**
     * Metodo para convertir un String a double.
     * @param  str   String
     * @param  pred  Valor pred
     * @return double
     */
    public static double strToDouble(final String str, final double pred) {
        try { 
            return Double.parseDouble(str);
        } catch (Exception e) {}
        return pred;
    }
    /**
     * Metodo para regresar un String a partir de un double.
     * @param  val   Valor
     * @return String
     */
    public static String floatToStr(final float val) {
        return String.valueOf(val);
    }
    
    /**
     * Metodo para convertir un String a entero.
     * @param  str    Strintr
     * @return float
     */    
    public static float strToFloat(final String str) {
    	return strToFloat(str, -1);
    }
    /**
     * Metodo para convertir un String a double.
     * @param  str   String
     * @param  pred  Valor pred
     * @return double
     */
    public static float strToFloat(final String str, final float pred) {
        try { 
            return Float.parseFloat(str);
        } catch (Exception e) {}
        return pred;
    }
    
    public static boolean strToBoolean(final String str) {
    	return strToBoolean(str, false);
    }
    
    public static boolean strToBoolean(final String str, final boolean pred) {
        try { 
            return Boolean.parseBoolean(str);
        } catch (Exception e) {}
        return pred;
    }
    
    /**
     * Metodo para convertir un String a entero.
     * @param  str   String
     * @return int
     */
    public static int strToInt(final String str) { 
        return strToInt(str, -1);
    }
    public static long strToLong(final String str) {
    	return strToLong(str, -1);
    }
    public static long strToLong(final String str, long pred) {
    	try { 
    		return Long.parseLong(str);
    	} catch (Exception e) {}
    	return pred;
    }
    /**
     * Metodo para regresar un String a partir de un int.
     * @param  val    Valor
     * @return String
     */
    public static String intToStr(final int val) { 
        return String.valueOf(val);
    }
    /**
     * Metodo para convertir un string a entero, 
     * y tambien tiene un valor predeterminado.
     * @param  str   String
     * @param  pred  Valor predeterminado 
     * @return int
     */
    public static int strToInt(final String str, final int pred) { 
        try { 
            return Integer.parseInt(str);
        } catch (Exception e) {}
        return pred;
    }
    /**
     * Metodo para regresar un String a partir de un long.
     * @param  val    Valor
     * @return String
     */
    public static String longToStr(final long val) { 
        return String.valueOf(val);
    }
    
    /**
     * Obtiene un reemplazo de strings. 
     * 
     * Aqui un ejemplo: 
     *     src = "Hola <mundo> 123 "
     *     comodin = "<mundo>";
     *     newWordComoding = "alfonso";
     *     result = "Hola alfonso 123";
     * @param  src                String fuente
     * @param  comodin            Comodin a utilizar
     * @param  newWordComodin     Nueva palabra de reemplazo
     * @return String
     */
    public String getMessageReplace(final String src, 
        final String comodin, final String newWordComodin) {   
        return src.replace(comodin, newWordComodin);
    }
    /**
     * Metodo para reemplazar caracteres.
     * @param  cadena    Cadena original
     * @param  oldChar   Caracteres viejos 
     * @param  newChar   Caracteres nuevos
     * @return String
     */
    public static String replaceCharacters(final String cadena, 
        final String oldChar, final String newChar) {
    
        return cadena.replace(oldChar, newChar);
    }
    /**
     * Metodo que regresa la hora actual en formato de String.
     * @return  String   Hora actual
     */
    public static String getTime() {
        Calendar calendario = new GregorianCalendar();

        return "Start: " + calendario.get(Calendar.HOUR) + ":" 
            + calendario.get(Calendar.MINUTE) + ":" 
            + calendario.get(Calendar.SECOND) + ":" 
            + calendario.get(Calendar.MILLISECOND);
    }

    public static java.util.Date getDateToday(final String format) { 
    	return transformStringToDate(null, format);
    }
        
    public static String getDateStringToday(final String format) {
    	Date fechaHoy = new Date();
    	SimpleDateFormat formatter = new SimpleDateFormat(format);    	
    	return formatter.format(fechaHoy);
    }
    
    public static String startDate(String dateStr) { 
    	 return dateStr + START_DATE_RANGE;
    }
    
    public static String endDate(String dateStr) { 
   	 	return dateStr + END_DATE_RANGE;
    }
       
    public static java.util.Date transformStringToDate(final String dateStr, String format) {
    	DateFormat df = new SimpleDateFormat(format);
    	java.util.Date date = null;
    	try {
    		if (dateStr == null) { 
    			date = new Date();
    		} else {
    			date = df.parse(dateStr);
    		}
    		Logs.debug(Utilerias.class, "Date parsed: " + df.format(date));
    	} catch (ParseException e) {
    		Logs.debug(Utilerias.class, "Error en el metodo transformStringToDate: " + e.toString());
    	}
    	return date;
    }
    
    /**
     * Metodo para formatear un java.sql.Date.
     * @param  date    Fecha a formatear
     * @param  format  String de formato
     * @return String
     */
    public static String getDate(final Date date, final String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);

        if (date == null) {
        	return "";
        }
        return formatter.format(date);
    }

    /**
     * Metodo para formatear un java.sql.Time.
     * @param  time    Tiempo
     * @param  format  String de formato
     * @return String
     */
    public static String getTime(final java.sql.Time time, final String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        if (time == null) {
        	return "";
        }
        return formatter.format(time);
    }

    public static String removeCharactersLeft(String str, String chac) {
    	String uno = "";
    	try {
	    	String dos = new String((chac.charAt(0) + "").getBytes(), "UTF-8");
	    	for (int i=0; i < str.length(); i++) {
	    		uno = new String((str.charAt(i) + "").getBytes(), "UTF-8");
	    		if (!uno.equals(dos)) {
	    			return str.substring(i);
	    		}
	    	}
    	} catch (UnsupportedEncodingException e) {
    		Logs.error(Utilerias.class, "Error: " + e.toString());
    	}
    	return ""; 
    }
        
    public static String removeDecimalRight(String str) {
    	StringBuilder sb = new StringBuilder();
    	try { 
    		sb.append(str.substring(0, str.indexOf(".")));
    	} catch (Exception e) {
    		sb.append(sb);
    		Logs.error(Utilerias.class, "Error: " + e.toString());    		
    	}
    	return sb.toString();
    }
    
    public static String getCurrencyFormat(Object obj) {
    	//return obj.toString();
    	return currencyFormatter.format(obj);    	
    }    
    
    public static String getDecimalFormat(Object obj) {
    	return formateador.format(obj);
    }
    
    public static String getMessageValue(String key, String value) {
    	if (key != null && value != null) {
    		if (key.equals(YES_NO_KEY)) { 
		    	if (value.equals(YES_VALUE)) { 
		    		return YES_DESC;
		    	} else if (value.equals(NO_VALUE)) {
		    		return NO_DESC;
		    	}
    		} else if (key.equals(TIPO_PRODUCTO_KEY)) {
    			if (value.equals(TIPO_PRODUCTO_NORMAL_VALUE)) {
    				return TIPO_PRODUCTO_NORMAL_DESC;
    			} else if (value.equals(TIPO_PRODUCTO_SUSCRIPCION_VALUE)) { 
    				return TIPO_PRODUCTO_SUSCRIPCION_DESC;
    			} else if (value.equals(TIPO_PRODUCTO_VENTA_ESPECIAL_VALUE)) {
    				return TIPO_PRODUCTO_VENTA_ESPECIAL_DESC;
    			}
    		}
	    }
    	return "";
    }
    
    public static String getDescripcionesBD(String desc, String value) {
    	StringBuilder sb = new StringBuilder();    	
    	if (desc != null && value != null) { 
	    	if (desc.equals(Utilerias.ESTATUS_PAGO_COMPRA)) { 
	    		if (value.equals(PAGO_CREDITO)) { 
	    			sb.append(DESC_PAGO_CREDITO);
	    		} else if (value.equals(PAGO_CONTADO)) { 
	    			sb.append(DESC_PAGO_CONTADO);
	    		} else if (value.equals(PAGO_INVENTARIO)) { 
	    			sb.append(DESC_PAGO_INVENTARIO);	
	    		} else {
	    			Logs.debug(Utilerias.class, "no hay desc : getDescripcionesBD");
	    		}
	    	} else if (desc.equals(Utilerias.ESTATUS_PAGO_VENTA)) {
	    		if (value.equals(PAGO_CREDITO)) { 
	    			sb.append(DESC_PAGO_CREDITO);
	    		} else if (value.equals(PAGO_CONTADO)) { 
	    			sb.append(DESC_PAGO_CONTADO);
	    		} else {
	    			Logs.debug(Utilerias.class, "no hay desc : getDescripcionesBD");
	    		}
	    	}
    	}
    	return sb.toString();    	
    }

    public static boolean checkList(List<Object[]> listaGenerica) {
		if (listaGenerica != null && listaGenerica.size() > 0 && listaGenerica.get(0) != null) {
			return true;
		}
		return false;
	}

    public static void agregarMensaje(StringBuffer sb, List<Object[]> list, String mensajeInicial, String mensajeFinal) {  
		sb.append(mensajeInicial);
		for (int i=0; i < list.size(); i++) { 
			Object[] values = list.get(i);
			sb.append(values[0].toString());
			if (i+1 < list.size()) { 
				sb.append(DELIMITADOR_COMA);
			}
		}
		sb.append(DELIMITADOR_PUNTO + BREAK_NEW_LINE_HTML);
		sb.append(mensajeFinal + BREAK_NEW_LINE_HTML);
    }
	
    /**
     * Metodo para realizar ciertas pruebas referentes a la clase en particular.
     * @param args  Argumentos de consola
     */
    public static void main(final String[] args) {
                //String creditNumber = "12345678901238";
        //String creditNumber = "12345678912320";
        //Utilerias.debug(Utilerias.class,
        //    "valor resultado = " 
        //        + Utilerias.isCreditNumberAccept(creditNumber));
        
        //Utilerias.debug(Utilerias.class, Utilerias.replaceCharacters("mauricio,gomez,torres", ",", ""));
        
        Logs.debug(Utilerias.class, Utilerias.removeCharactersLeft("???????80502450000000000000", "?"));
        //Utilerias.debug(Utilerias.class, Utilerias.getFourCifrasDecimal("00004234234000"));
        
        Logs.debug(Utilerias.class, Utilerias.getDateStringToday(Utilerias.FORMAT_DATE));
        
        Utilerias.transformStringToDate("2011-7-31" + " 23:59:59", Utilerias.FORMAT_DATE_WITH_HOUR);
        
        System.out.println("Data: " + Utilerias.replaceCharacters("Hola mundo " + BREAK_NEW_LINE_HTML, BREAK_NEW_LINE_HTML, BREAK_LINE_HTML));
        
        //Utilerias.debug(Utilerias.class, Utilerias.transformStringToDate("2011-7-31" + " 00:00:000", Utilerias.FORMAT_DATE_WITH_HOUR));
    }        

}
