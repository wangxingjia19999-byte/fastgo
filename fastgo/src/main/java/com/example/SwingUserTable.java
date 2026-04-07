package com.example;


import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import org.apache.ibatis.session.SqlSession;

public class SwingUserTable {
    private static DefaultTableModel tableModel;
    private static JTable table;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SwingUserTable::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("test表数据管理");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 500);

        String[] columnNames = {"ID", "Name", "Content", "Num", "Create Time", "Update Time"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 只允许除ID、创建/更新时间外的字段可编辑
                return column == 1 || column == 2 || column == 3;
            }
        };
        table = new JTable(tableModel);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel buttonPanel = new JPanel();
        JButton addBtn = new JButton("新增");
        JButton updateBtn = new JButton("修改");
        JButton deleteBtn = new JButton("删除");
        JButton refreshBtn = new JButton("刷新");
        buttonPanel.add(addBtn);
        buttonPanel.add(updateBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);

        addBtn.addActionListener(e -> addUser());
        updateBtn.addActionListener(e -> updateUser());
        deleteBtn.addActionListener(e -> deleteUser());
        refreshBtn.addActionListener(e -> refreshTable());

        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void refreshTable() {
        tableModel.setRowCount(0);
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            UserMapper mapper = session.getMapper(UserMapper.class);
            List<User> users = mapper.selectAll();
            for (User u : users) {
                tableModel.addRow(new Object[]{
                        u.getId(), u.getName(), u.getContent(), u.getNum(), u.getCreateTime(), u.getUpdateTime()
                });
            }
        }
    }

    private static void addUser() {
        JTextField nameField = new JTextField();
        JTextField contentField = new JTextField();
        JTextField numField = new JTextField();
        Object[] fields = {
                "Name:", nameField,
                "Content:", contentField,
                "Num:", numField
        };
        int result = JOptionPane.showConfirmDialog(null, fields, "新增记录", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try (SqlSession session = MyBatisUtil.getSqlSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                User user = new User();
                user.setName(nameField.getText());
                user.setContent(contentField.getText());
                try {
                    user.setNum(Integer.parseInt(numField.getText()));
                } catch (NumberFormatException e) {
                    user.setNum(0);
                }
                mapper.insert(user);
                session.commit();
            }
            refreshTable();
        }
    }

    private static void updateUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "请先选择要修改的行");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        String name = (String) tableModel.getValueAt(row, 1);
        String content = (String) tableModel.getValueAt(row, 2);
        int num = (int) tableModel.getValueAt(row, 3);
        try (SqlSession session = MyBatisUtil.getSqlSession()) {
            try (java.sql.PreparedStatement ps = session.getConnection()
                    .prepareStatement("UPDATE test SET name = ?, content = ?, num = ? WHERE id = ?")) {
                ps.setString(1, name);
                ps.setString(2, content);
                ps.setInt(3, num);
                ps.setInt(4, id);
                ps.executeUpdate();
            } catch (java.sql.SQLException e) {
                throw new RuntimeException("更新记录失败", e);
            }
            session.commit();
        }
        refreshTable();
    }

    private static void deleteUser() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "请先选择要删除的行");
            return;
        }
        int id = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(null, "确定要删除ID=" + id + "的记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try (SqlSession session = MyBatisUtil.getSqlSession()) {
                UserMapper mapper = session.getMapper(UserMapper.class);
                mapper.delete(id);
                session.commit();
            }
            refreshTable();
        }
    }
}
