/**
 * 
 */
package com.indra.exception;

/**
 * @author aocarballo
 *
 */
public class ErrorLoginException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorLoginException(String mensaje) {
		super(mensaje);
		System.out.println(mensaje);
		System.exit(0);
	}
}
