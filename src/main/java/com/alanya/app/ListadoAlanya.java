package com.alanya.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.alanya.model.Prestamo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ListadoAlanya extends JFrame {

    private JPanel contentPane;
    private JTextArea txtSalida;
    private JButton btnListar, btnLimpiar;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                ListadoAlanya frame = new ListadoAlanya();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public ListadoAlanya() {
        setTitle("Listado de Préstamos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 550, 450);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("Préstamos Registrados");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 16));
        lblTitulo.setBounds(170, 10, 250, 25);
        contentPane.add(lblTitulo);

        txtSalida = new JTextArea();
     // evitar que el usuario escriba directamente
        txtSalida.setEditable(false); 
        JScrollPane scroll = new JScrollPane(txtSalida);
        scroll.setBounds(30, 50, 470, 280);
        contentPane.add(scroll);

        btnListar = new JButton("Listar");
        btnListar.setBounds(130, 350, 120, 30);
        btnListar.addActionListener(e -> mostrarListado());
        contentPane.add(btnListar);

        btnLimpiar = new JButton("Limpiar");
        btnLimpiar.setBounds(290, 350, 120, 30);
        btnLimpiar.addActionListener(e -> txtSalida.setText(""));
        contentPane.add(btnLimpiar);
    }

    void mostrarListado() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
        EntityManager em = emf.createEntityManager();

        List<Prestamo> prestamos = em.createQuery("SELECT p FROM Prestamo p", Prestamo.class)
                .getResultList();

        txtSalida.setText("Listado de Préstamos\n");
        txtSalida.append("---------------------------------------------------\n");

        for (Prestamo p : prestamos) {
            txtSalida.append("Fecha...: " + p.getFecha() + "\n");
            txtSalida.append("Usuario.: " + p.getUsuario().getNombre() + "\n");
            txtSalida.append("Libro...: " + p.getLibro().getTitulo() + "\n");
            txtSalida.append("---------------------------------------------------\n");
        }

        em.close();
        emf.close();
    }
}
