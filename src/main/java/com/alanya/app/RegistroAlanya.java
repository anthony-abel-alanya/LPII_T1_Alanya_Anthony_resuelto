package com.alanya.app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.alanya.model.Libro;
import com.alanya.model.Prestamo;
import com.alanya.model.Usuario;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class RegistroAlanya extends JFrame {

    private JPanel contentPane;
    private JComboBox<Usuario> cboUsuarios;
    private JComboBox<Libro> cboLibros;
    private JComboBox<String> cboEstado;
    private JTextArea txtSalida;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
            	RegistroAlanya  frame = new RegistroAlanya ();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public RegistroAlanya () {
        setTitle("Registro de Préstamos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 500, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblUsuario = new JLabel("Usuario:");
        lblUsuario.setBounds(30, 30, 100, 20);
        contentPane.add(lblUsuario);

        cboUsuarios = new JComboBox<>();
        cboUsuarios.setBounds(130, 30, 300, 22);
        contentPane.add(cboUsuarios);

        JLabel lblLibro = new JLabel("Libro:");
        lblLibro.setBounds(30, 70, 100, 20);
        contentPane.add(lblLibro);

        cboLibros = new JComboBox<>();
        cboLibros.setBounds(130, 70, 300, 22);
        contentPane.add(cboLibros);

        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setBounds(30, 110, 100, 20);
        contentPane.add(lblEstado);

        cboEstado = new JComboBox<>(new String[]{"Emitido", "Cancelado", "Anulado"});
        cboEstado.setBounds(130, 110, 150, 22);
        contentPane.add(cboEstado);

        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.setBounds(180, 150, 120, 25);
        btnRegistrar.addActionListener(e -> registrar());
        contentPane.add(btnRegistrar);

        txtSalida = new JTextArea();
        JScrollPane scroll = new JScrollPane(txtSalida);
        scroll.setBounds(30, 190, 400, 150);
        contentPane.add(scroll);

        llenarCombos();
    }

    void llenarCombos() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
        EntityManager em = emf.createEntityManager();

        // Usuarios
        List<Usuario> usuarios = em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
        for (Usuario u : usuarios) {
            cboUsuarios.addItem(u);
        }

        // Libros
        List<Libro> libros = em.createQuery("SELECT l FROM Libro l", Libro.class).getResultList();
        for (Libro l : libros) {
            cboLibros.addItem(l);
        }

        em.close();
        emf.close();
    }

    void registrar() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("mysql");
        EntityManager em = emf.createEntityManager();

        try {
            Prestamo p = new Prestamo();

            Usuario usuario = (Usuario) cboUsuarios.getSelectedItem();
            Libro libro = (Libro) cboLibros.getSelectedItem();
            String estado = (String) cboEstado.getSelectedItem();

            p.setUsuario(usuario);
            p.setLibro(libro);
            p.setEstado(Prestamo.Estado.valueOf(estado));
            p.setFecha(LocalDate.now().toString());

            em.getTransaction().begin();
            em.persist(p);
            em.getTransaction().commit();

            txtSalida.setText("Préstamo registrado exitosamente.");

        } catch (Exception e) {
            em.getTransaction().rollback();
            txtSalida.setText("Error al registrar el préstamo.");
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }
}
