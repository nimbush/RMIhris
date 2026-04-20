package com.techspire.project.client.view;

import com.techspire.project.client.controller.ClientController;
import com.techspire.project.client.view.ui.BasePanel;
import com.techspire.project.client.view.ui.DarkTheme;
import com.techspire.project.client.view.ui.DialogUtil;
import com.techspire.project.client.view.ui.ModernButton;
import com.techspire.project.dto.LeaveApplyDto;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class ApplyLeavePanel extends BasePanel {

    private final ClientController controller;
    private final JTextField icNumberField;
    private final JDateChooser fromDateChooser;
    private final JDateChooser toDateChooser;
    private final JTextField leaveDaysField;
    private final ModernButton applyButton;

    public ApplyLeavePanel(ClientController controller, String icNumber) {
        this.controller = controller;

        this.icNumberField = new JTextField(24);
        this.fromDateChooser = new JDateChooser();
        this.toDateChooser = new JDateChooser();
        this.leaveDaysField = new JTextField(24);
        this.applyButton = new ModernButton("Apply Leave");

        this.icNumberField.setText(icNumber);
        this.icNumberField.setEditable(false);
        this.leaveDaysField.setEditable(false);

        initUi();
        registerEvents();
    }

    private void initUi() {
        setLayout(new GridBagLayout());
        setBackground(DarkTheme.BG);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(DarkTheme.PANEL);
        card.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(24, 24, 24, 24)
        ));

        Font titleFont = new Font("Segoe UI", Font.BOLD, 22);
        Font labelFont = new Font("Segoe UI", Font.BOLD, 15);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 15);

        Dimension fieldSize = new Dimension(320, 42);
        Dimension buttonSize = new Dimension(220, 44);

        JLabel titleLabel = new JLabel("Apply Leave");
        titleLabel.setForeground(DarkTheme.TEXT);
        titleLabel.setFont(titleFont);

        styleTextField(icNumberField, fieldFont, fieldSize, true);
        styleTextField(leaveDaysField, fieldFont, fieldSize, true);

        styleDateChooser(fromDateChooser, fieldFont, fieldSize);
        styleDateChooser(toDateChooser, fieldFont, fieldSize);

        applyButton.setPreferredSize(buttonSize);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 20, 10);
        card.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 1;
        card.add(createLabel("IC Number:", labelFont), gbc);

        gbc.gridx = 1;
        card.add(icNumberField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        card.add(createLabel("From Date:", labelFont), gbc);

        gbc.gridx = 1;
        card.add(fromDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        card.add(createLabel("To Date:", labelFont), gbc);

        gbc.gridx = 1;
        card.add(toDateChooser, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        card.add(createLabel("Leave Days:", labelFont), gbc);

        gbc.gridx = 1;
        card.add(leaveDaysField, gbc);

        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 0, 10);
        card.add(applyButton, gbc);

        add(card);
    }

    private JLabel createLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(DarkTheme.TEXT);
        return label;
    }

    private void styleTextField(JTextField field, Font font, Dimension size, boolean readOnly) {
        field.setFont(font);
        field.setPreferredSize(size);
        field.setBackground(new Color(28, 28, 28));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(8, 12, 8, 12)
        ));
    }

    private void styleDateChooser(JDateChooser chooser, Font font, Dimension size) {
        chooser.setFont(font);
        chooser.setPreferredSize(size);
        chooser.setBackground(DarkTheme.PANEL);
        chooser.setBorder(new CompoundBorder(
                new LineBorder(DarkTheme.BORDER, 1, true),
                new EmptyBorder(0, 0, 0, 0)
        ));
        chooser.setDateFormatString("yyyy-MM-dd");

        JComponent editor = (JComponent) chooser.getDateEditor().getUiComponent();
        editor.setFont(font);
        editor.setBackground(new Color(28, 28, 28));
        editor.setForeground(Color.WHITE);
        editor.setBorder(new EmptyBorder(8, 12, 8, 12));

        if (editor instanceof JTextComponent textComponent) {
            textComponent.setCaretColor(DarkTheme.TEXT);
            textComponent.setSelectionColor(DarkTheme.ACCENT);
            textComponent.setSelectedTextColor(Color.WHITE);
        }

        for (Component c : chooser.getComponents()) {
            if (c instanceof JButton btn) {
                btn.setPreferredSize(new Dimension(42, 42));
                btn.setBackground(new Color(28, 28, 28));
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFocusPainted(false);
                btn.setOpaque(true);
                btn.setContentAreaFilled(false);
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
        }

        SwingUtilities.invokeLater(() -> {
            for (Component c : chooser.getComponents()) {
                if (c instanceof JButton btn) {
                    btn.repaint();
                }
            }
            styleCalendarPopup(chooser);
        });
    }

    private void styleCalendarPopup(JDateChooser chooser) {
        try {
            JCalendar calendar = chooser.getJCalendar();
            if (calendar == null) {
                return;
            }

            calendar.setBackground(new Color(24, 24, 24));
            calendar.setForeground(DarkTheme.TEXT);
            calendar.setBorder(new CompoundBorder(
                    new LineBorder(DarkTheme.ACCENT, 2, true),
                    new EmptyBorder(8, 8, 8, 8)
            ));
            calendar.setPreferredSize(new Dimension(320, 260));
            calendar.setMinimumSize(new Dimension(320, 260));
            calendar.setMaximumSize(new Dimension(320, 260));
            calendar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            styleMonthChooser(calendar.getMonthChooser());
            styleYearChooser(calendar.getYearChooser());
            styleDayChooser(calendar.getDayChooser());

            calendar.revalidate();
            calendar.repaint();

        } catch (Exception ignored) {
        }
    }

    private void styleMonthChooser(JMonthChooser monthChooser) {
        if (monthChooser == null) {
            return;
        }

        monthChooser.setBackground(new Color(24, 24, 24));
        monthChooser.setForeground(DarkTheme.TEXT);

        for (Component c : monthChooser.getComponents()) {
            if (c instanceof JComboBox<?> comboBox) {
                comboBox.setFont(new Font("Segoe UI", Font.BOLD, 14));
                comboBox.setPreferredSize(new Dimension(140, 28));
                comboBox.setBackground(Color.BLACK);
                comboBox.setForeground(Color.WHITE);
                comboBox.setBorder(new CompoundBorder(
                        new LineBorder(DarkTheme.BORDER, 1, true),
                        new EmptyBorder(2, 8, 2, 8)
                ));
                comboBox.setFocusable(false);

                comboBox.setUI(new BasicComboBoxUI() {
                    @Override
                    protected JButton createArrowButton() {
                        return new JButton() {
                            {
                                setPreferredSize(new Dimension(28, 28));
                                setBackground(Color.BLACK);
                                setForeground(Color.WHITE);
                                setBorder(BorderFactory.createEmptyBorder());
                                setFocusPainted(false);
                                setOpaque(true);
                                setContentAreaFilled(false);
                            }

                            @Override
                            protected void paintComponent(Graphics g) {
                                g.setColor(Color.BLACK);
                                g.fillRect(0, 0, getWidth(), getHeight());

                                g.setColor(Color.WHITE);
                                int x = getWidth() / 2;
                                int y = getHeight() / 2;
                                int size = 5;
                                Polygon arrow = new Polygon();
                                arrow.addPoint(x - size, y - 2);
                                arrow.addPoint(x + size, y - 2);
                                arrow.addPoint(x, y + size);
                                g.fillPolygon(arrow);
                            }
                        };
                    }
                });

                comboBox.setRenderer(new DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus
                    ) {
                        JLabel label = (JLabel) super.getListCellRendererComponent(
                                list, value, index, isSelected, cellHasFocus
                        );

                        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                        label.setOpaque(true);
                        label.setBorder(new EmptyBorder(6, 8, 6, 8));

                        if (index == -1) {
                            label.setBackground(Color.BLACK);
                            label.setForeground(Color.WHITE);
                        } else if (isSelected) {
                            label.setBackground(DarkTheme.ACCENT);
                            label.setForeground(Color.WHITE);
                        } else {
                            label.setBackground(Color.BLACK);
                            label.setForeground(Color.WHITE);
                        }

                        return label;
                    }
                });

                SwingUtilities.invokeLater(() -> {
                    for (Component child : comboBox.getComponents()) {
                        if (child instanceof JButton btn) {
                            btn.setBackground(Color.BLACK);
                            btn.setForeground(Color.WHITE);
                            btn.setOpaque(true);
                            btn.setContentAreaFilled(false);
                            btn.setBorder(BorderFactory.createEmptyBorder());
                            btn.setFocusPainted(false);
                            btn.repaint();
                        }
                    }
                });
            }
        }
    }

    private void styleYearChooser(JYearChooser yearChooser) {
        if (yearChooser == null) {
            return;
        }

        yearChooser.setBackground(new Color(24, 24, 24));
        yearChooser.setForeground(DarkTheme.TEXT);

        for (Component c : yearChooser.getComponents()) {
            if (c instanceof JTextField field) {
                field.setFont(new Font("Segoe UI", Font.BOLD, 14));
                field.setBackground(new Color(30, 30, 30));
                field.setForeground(Color.WHITE);
                field.setCaretColor(Color.WHITE);
                field.setBorder(new CompoundBorder(
                        new LineBorder(DarkTheme.BORDER, 1, true),
                        new EmptyBorder(4, 8, 4, 8)
                ));
            }

            if (c instanceof JButton btn) {
                btn.setBackground(new Color(30, 30, 30));
                btn.setForeground(Color.WHITE);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFocusPainted(false);
                btn.setOpaque(true);
                btn.setContentAreaFilled(true);
            }
        }
    }

    private void styleDayChooser(JDayChooser dayChooser) {
        if (dayChooser == null) {
            return;
        }

        dayChooser.setBackground(new Color(24, 24, 24));
        dayChooser.setForeground(DarkTheme.TEXT);

        Component[] components = dayChooser.getComponents();
        Calendar today = Calendar.getInstance();

        for (Component c : components) {
            if (c instanceof JLabel label) {
                label.setOpaque(true);
                label.setBackground(new Color(200, 216, 226));
                label.setForeground(new Color(25, 103, 210));
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 13));
                label.setBorder(new EmptyBorder(6, 6, 6, 6));

                if ("Sun".equalsIgnoreCase(label.getText())) {
                    label.setForeground(new Color(234, 67, 53));
                }
            }

            if (c instanceof JButton btn) {
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createEmptyBorder());
                btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
                btn.setPreferredSize(new Dimension(38, 28));
                btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                btn.setOpaque(true);

                String text = btn.getText() != null ? btn.getText().trim() : "";
                if (text.isEmpty()) {
                    btn.setBackground(new Color(24, 24, 24));
                    btn.setForeground(new Color(90, 90, 90));
                    continue;
                }

                btn.setBackground(new Color(24, 24, 24));
                btn.setForeground(new Color(232, 234, 237));

                try {
                    int day = Integer.parseInt(text);
                    if (day == today.get(Calendar.DAY_OF_MONTH)) {
                        btn.setBackground(new Color(60, 64, 67));
                        btn.setForeground(Color.WHITE);
                    }
                } catch (NumberFormatException ignored) {
                }
            }
        }

        try {
            Field selectedDayField = JDayChooser.class.getDeclaredField("selectedDay");
            selectedDayField.setAccessible(true);
            Object selectedDayObj = selectedDayField.get(dayChooser);

            if (selectedDayObj instanceof JButton selectedButton) {
                selectedButton.setBackground(new Color(189, 189, 189));
                selectedButton.setForeground(new Color(198, 40, 40));
                selectedButton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
                selectedButton.setOpaque(true);
            }
        } catch (Exception ignored) {
        }
    }

    private void registerEvents() {
        applyButton.addActionListener(e -> applyLeave());

        fromDateChooser.getDateEditor().addPropertyChangeListener(evt -> {
            updateLeaveDays();
            SwingUtilities.invokeLater(() -> styleCalendarPopup(fromDateChooser));
        });

        toDateChooser.getDateEditor().addPropertyChangeListener(evt -> {
            updateLeaveDays();
            SwingUtilities.invokeLater(() -> styleCalendarPopup(toDateChooser));
        });
    }

    private void updateLeaveDays() {
        Date from = fromDateChooser.getDate();
        Date to = toDateChooser.getDate();

        if (from == null || to == null) {
            leaveDaysField.setText("");
            return;
        }

        LocalDate fromLocal = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate toLocal = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (toLocal.isBefore(fromLocal)) {
            leaveDaysField.setText("");
            return;
        }

        long days = ChronoUnit.DAYS.between(fromLocal, toLocal) + 1;
        leaveDaysField.setText(String.valueOf(days));
    }

    private void applyLeave() {
        try {
            Date from = fromDateChooser.getDate();
            Date to = toDateChooser.getDate();

            if (from == null || to == null) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(
                        this,
                        "Please select both From Date and To Date.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            LocalDate fromLocal = from.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate toLocal = to.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (toLocal.isBefore(fromLocal)) {
                DialogUtil.styleMessageDialog();
                JOptionPane.showMessageDialog(
                        this,
                        "To Date cannot be before From Date.",
                        "Validation Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int leaveDays = (int) (ChronoUnit.DAYS.between(fromLocal, toLocal) + 1);

            boolean confirmed = DialogUtil.showConfirmDialog(
                    this,
                    "Confirm Leave Application",
                    "Are you sure you want to apply leave?"
            );

            if (!confirmed) {
                return;
            }

            LeaveApplyDto dto = new LeaveApplyDto();
            dto.setIcNumber(icNumberField.getText().trim());
            dto.setFromDate(fromLocal);
            dto.setToDate(toLocal);
            dto.setLeaveDays(leaveDays);

            String result = controller.applyLeave(dto);

            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(
                    this,
                    result,
                    "Leave Application",
                    JOptionPane.INFORMATION_MESSAGE
            );

            fromDateChooser.setDate(null);
            toDateChooser.setDate(null);
            leaveDaysField.setText("");

        } catch (Exception ex) {
            ex.printStackTrace();
            DialogUtil.styleMessageDialog();
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}