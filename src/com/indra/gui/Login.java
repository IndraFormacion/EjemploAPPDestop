package com.indra.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import com.indra.exception.ErrorLoginException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;

public class Login extends JFrame {
	int contador=0;
	private JPanel contentPane;
	private JTextField tfUsuario;
	private JTextField tfPasswd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblusuario = new JLabel("Usuario");
		
		tfUsuario = new JTextField();
		tfUsuario.setColumns(10);
		
		JLabel lblpasswd = new JLabel("Contraseña");
		
		tfPasswd = new JTextField();
		tfPasswd.setColumns(10);
		
		JButton btnAceptar = new JButton("Aceptar");
		btnAceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Acciones a realizar cuando pulsamos el boton aceptar
				String usuario=tfUsuario.getText();
				String passwd=tfPasswd.getText();
				String passwdEncrip="";
				//Encriptamos la contraseña
				try {
					passwdEncrip=encriptar(passwd);
					System.out.println("encriptando .....");
					System.out.println(passwdEncrip);
					
				} catch (Exception ex1) {
					ex1.printStackTrace();
				}
				
				// Conectamos a la BD
				String url="jdbc:mysql://localhost:3306/indra2022";
		        Connection con;
		        Statement stmt;
		        String query = "select nombre,passwd from empleados where nombre='"+usuario+"' and passwd='"+passwdEncrip+"'";
		        
		        try {
		            con=DriverManager.getConnection(url, "root", "123456");
		            stmt=con.createStatement();
		            //stmt.executeUpdate(query); --> da una respuesta booleana
		            ResultSet rs=stmt.executeQuery(query);
		            if(rs.next()) {
		            		System.out.println("Login correcto");
		            
		            }else {
		            	System.out.println("Login incorrecto");
		            	contador++;
		            	if(contador>=3) {
		            		try {
								errorLogin();
							} catch (ErrorLoginException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		            	}
		            }
		            
		                    
		            stmt.close();
		            con.close();                
		        }catch(SQLException ex2) {
		            System.err.println(ex2.getMessage());
		        }
			}
		});
		
		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfUsuario.setText("");
				tfPasswd.setText("");
			}
		});
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGap(35)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING, false)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(lblpasswd)
								.addComponent(lblusuario))
							.addGap(18)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
								.addComponent(tfUsuario)
								.addComponent(tfPasswd, GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnAceptar)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnCancelar)))
					.addGap(85))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(29)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblusuario)
						.addComponent(tfUsuario, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblpasswd)
						.addComponent(tfPasswd, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(32)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnCancelar)
						.addComponent(btnAceptar))
					.addContainerGap(109, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
	private  String miHash(String clear) throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");// message digest

		byte[] b = md.digest(clear.getBytes());
		int size = b.length;
		StringBuffer h = new StringBuffer(size);
		for (int i = 0; i < size; i++) {
			int u = b[i] & 255; // unsigned conversion
			if (u < 16) {
				h.append("0" + Integer.toHexString(u));
			} else {
				h.append(Integer.toHexString(u));
			}
		}
		return h.toString();
	}
	
	public  String encriptar(String palabra) throws Exception {
		String cripto="";		
	
		try {
			cripto=miHash(palabra);
		} catch (Exception e) {
			throw new Error("Error al encriptar la palabra");
		}
				
		return cripto;
	}
	public void errorLogin() throws ErrorLoginException{
		throw new ErrorLoginException("Error en login superado el maximo de intentos, saliendo del programa");
	}
}
