//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//::                                                                         ::
//::     Antonio Manuel Rodrigues Manso                                      ::
//::                                                                         ::
//::     I N S T I T U T O    P O L I T E C N I C O   D E   T O M A R        ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::     e-mail: manso@ipt.pt                                                ::
//::     url   : http://orion.ipt.pt/~manso                                  ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                                         ::
//::                                                               (c)2020   ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////
package Election.gui;

import Election.core.ElectionCore;
import Election.distributed.RemoteInterface;
import Election.distributed.utils.RMI;
import Election.wallet.User;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author manso
 */
public class Administration extends javax.swing.JFrame {

    public static String fileElection = "Election.obj";
    ElectionCore election;
    private User user;
    private DefaultListModel<String> candidatesListModel;  // Add this line to declare the data model
    RemoteInterface remote;
    /**
     * Creates new form TemplarCoinGUI
     *
     * @param user
     */
    public Administration(User user) {
        
        this.user = user;
        candidatesListModel = new DefaultListModel<>();  // Initialize the data model
        
        try {
            remote = (RemoteInterface) RMI.getRemote(obterEnderecoRede());
            
        } catch (Exception ex) {
            Logger.getLogger(Administration.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Init GUI
        initComponents();
        setDefaultDateTimeValues();
        
        //Init ElectionCore
        try {
            ElectionCore.load(fileElection);
        } catch (Exception ex) {
            Logger.getLogger(Administration.class.getName()).log(Level.SEVERE, null, ex);
        }   
    
        setSize(800, 600);
        setLocationRelativeTo(null);

    }

     private static String obterEnderecoRede() {
        String enderecoRede = "";

        // Loop até que uma entrada não vazia seja fornecida
        while (enderecoRede.isEmpty()) {
            enderecoRede = JOptionPane.showInputDialog(null, "Introduza o endereço da rede:");

            // Verificar se o usuário cancelou a entrada
            if (enderecoRede == null) {
                // O usuário pressionou Cancelar, você pode lidar com isso aqui se necessário
                System.exit(0);
            }

            // Verificar se a entrada é vazia e exibir uma mensagem de alerta
            if (enderecoRede.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Por favor, insira um endereço da rede válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

        return enderecoRede;
    }
     
    private void updateStartDays() {
        updateDaysComboBox(startDayComboBox, startMonthComboBox, startYearComboBox);
    }

    private void updateEndDays() {
        updateDaysComboBox(endDayComboBox, endMonthComboBox, endYearComboBox);
    }

    private void updateDaysComboBox(JComboBox<String> dayComboBox, JComboBox<String> monthComboBox, JComboBox<String> yearComboBox) {
        // Obtém o número de dias do mês selecionado
        String mesSelecionado = monthComboBox.getSelectedItem().toString();
        String anoSelecionado = yearComboBox.getSelectedItem().toString();
        String diaSelecionado = dayComboBox.getSelectedItem().toString();  // Get the currently selected day
        int numeroDias = YearMonth.of(Integer.parseInt(anoSelecionado), Integer.parseInt(mesSelecionado)).lengthOfMonth();

        // Popula o JComboBox de dias com os números de 1 ao número de dias do mês
        String[] dias = new String[numeroDias];
        for (int i = 0; i < numeroDias; i++) {
            dias[i] = String.valueOf(i + 1);
        }

        // Remove os itens antigos e adiciona os novos
        dayComboBox.removeAllItems();
        for (String dia : dias) {
            dayComboBox.addItem(dia);
        }

        // Mantém o dia selecionado se existir na nova lista
        if (Arrays.asList(dias).contains(diaSelecionado)) {
            dayComboBox.setSelectedItem(diaSelecionado);
        }
    }

    private LocalDateTime getSelectedDateTime(JComboBox<String> hourComboBox, JComboBox<String> minComboBox, JComboBox<String> dayComboBox, JComboBox<String> monthComboBox, JComboBox<String> yearComboBox) {
        int selectedHour = Integer.parseInt(hourComboBox.getSelectedItem().toString());
        int selectedMin = Integer.parseInt(minComboBox.getSelectedItem().toString());
        int selectedDay = Integer.parseInt(dayComboBox.getSelectedItem().toString());
        int selectedMonth = Integer.parseInt(monthComboBox.getSelectedItem().toString());
        int selectedYear = Integer.parseInt(yearComboBox.getSelectedItem().toString());

        return LocalDateTime.of(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMin);
    }
    
    private LocalDateTime getStartSelectedDateTime() {
        return getSelectedDateTime(startHourComboBox, startMinComboBox, startDayComboBox, startMonthComboBox, startYearComboBox);
    }

    private LocalDateTime getEndSelectedDateTime() {
        return getSelectedDateTime(endHourComboBox, endMinComboBox, endDayComboBox, endMonthComboBox, endYearComboBox);
    }
    
    private void updateHoursAndMinutes(JComboBox<String> hourComboBox, JComboBox<String> minComboBox, JComboBox<String> dayComboBox, JComboBox<String> monthComboBox, JComboBox<String> yearComboBox, boolean isStart) {
        // Obter o DateTime inicial e final
        LocalDateTime startDateTime = getSelectedDateTime(startHourComboBox, startMinComboBox, startDayComboBox, startMonthComboBox, startYearComboBox);
        LocalDateTime endDateTime = getSelectedDateTime(endHourComboBox, endMinComboBox, endDayComboBox, endMonthComboBox, endYearComboBox);

        // Obter a hora atual e o minuto atual
        LocalDateTime currentTime = LocalDateTime.now();
        int currentHour = currentTime.getHour();
        int currentMin = currentTime.getMinute();

        // Criar arrays de horas e minutos
        List<String> hoursList = new ArrayList<>();
        List<String> minsList = new ArrayList<>();

        // Adicionar horas e minutos
        if (isStart) {
            // HORAS E MINUTOS PARA START
            int startHour = Integer.valueOf(hourComboBox.getSelectedItem().toString());
            int startMin = Integer.valueOf(minComboBox.getSelectedItem().toString());
            for (int hour = (currentHour == startHour ? startHour + 1 : startHour); hour <= 23; hour++) {
                hoursList.add(String.valueOf(hour));
            }
            for (int min = (currentHour == startHour ? (currentMin > startMin ? currentMin : startMin + 1) : 0); min <= 59; min++) {
                minsList.add(String.valueOf(min));
            }

            // Ajustar o endereço se necessário
            if (startDateTime.isAfter(endDateTime)) {
                endDateTime = startDateTime.plusMinutes(1); // Adicionar 1 minuto ao endereço
                //populateDays(endYearComboBox, endMonthComboBox, endDayComboBox, endDateTime.toLocalDate());
            }
        } else {
            // HORAS E MINUTOS PARA END
            int endHour = Integer.valueOf(hourComboBox.getSelectedItem().toString());
            int endMin = Integer.valueOf(minComboBox.getSelectedItem().toString());

            // Verificar se a data é a mesma
            if (startDateTime.toLocalDate().isEqual(endDateTime.toLocalDate())) {
                // Se a hora do final for a mesma ou posterior à do início
                if (endDateTime.getHour() >= startDateTime.getHour()) {
                    for (int min = (currentHour == endHour ? (currentMin > endMin ? currentMin : endMin + 1) : 0); min <= 59; min++) {
                        minsList.add(String.valueOf(min));
                    }
                } else {
                    // Se a hora do final for anterior à do início
                    for (int min = 0; min <= 59; min++) {
                        minsList.add(String.valueOf(min));
                    }
                }
            } else {
                // Se a data for diferente, incluir todos os minutos possíveis
                for (int min = 0; min <= 59; min++) {
                    minsList.add(String.valueOf(min));
                }
            }
        }

        // Definir modelos para ComboBoxes
        hourComboBox.setModel(new DefaultComboBoxModel<>(hoursList.toArray(new String[0])));
        minComboBox.setModel(new DefaultComboBoxModel<>(minsList.toArray(new String[0])));
    }


    private ComboBoxModel getNewModel(int ini, int fin){
        int curValue = fin-ini;
        String[] values = new String[curValue+1];
        for (int i = 0; i <= curValue; i++) {
            values[i] = String.valueOf(ini + i);
        }
        
        return new DefaultComboBoxModel<>(values);
    }   
    
    private void populateDays(JComboBox<String> yearComboBox, JComboBox<String> monthComboBox, JComboBox<String> dayComboBox) {
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        int currentDay = currentDate.getDayOfMonth();

        // Populate years - default year is the current year
        yearComboBox.setModel(getNewModel(currentYear,currentYear+10));
        
        // Populate months - default month is the current month goes up to 12
        monthComboBox.setModel(getNewModel(currentMonth,12));

        // Populate days - default day is the current day goes up to 28/30/31 
        int monthLength =YearMonth.of(currentYear, currentMonth).lengthOfMonth();
        dayComboBox.setModel(getNewModel(currentDay,monthLength));

        // Set current Day since it's its first call
        dayComboBox.setSelectedItem(String.valueOf(currentDay));
        
    }
 
    private void populateTimes(JComboBox<String> hourComboBox, JComboBox<String> minComboBox) {
        // Obter a hora atual e o minuto atual
        LocalTime currentTime = LocalTime.now();
        int currentHour = currentTime.getHour();
        int currentMin = currentTime.getMinute();

        // Populate Hours - default Hour is the current Hour goes up to 23
        hourComboBox.setModel(getNewModel(currentHour,23));

        // Populate Minutes - default minute is the current minute goes up to 59
        minComboBox.setModel(getNewModel(currentMin,59));
   }
   
    private void setDefaultDateTimeValues() {
        // Populate and set values for the start and end date default current date
        populateDays(startYearComboBox, startMonthComboBox, startDayComboBox);
        populateDays(endYearComboBox, endMonthComboBox, endDayComboBox);

        // Populate and set values for the start and end times default current time
        populateTimes(startHourComboBox, startMinComboBox);
        populateTimes(endHourComboBox, endMinComboBox);
    }

    private void updateMonthsAndDays(JComboBox<String> monthComboBox, JComboBox<String> dayComboBox, JComboBox<String> yearComboBox) {
        // Get the selected year and month
        String anoSelecionado = yearComboBox.getSelectedItem().toString();
        String mesSelecionado = monthComboBox.getSelectedItem().toString();

        // Populate months based on the selected year
        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        int currentMonth = currentDate.getMonthValue();
        int nMonths = (Integer.parseInt(anoSelecionado) == currentYear) ? 12 - (currentMonth - 1) : 12;

        String[] months = new String[nMonths];
        int startingMonth = (Integer.parseInt(anoSelecionado) == currentYear) ? currentMonth : 1;
        for (int i = 0; i < nMonths; i++) {
            months[i] = String.valueOf((startingMonth + i - 1) % 12 + 1);
        }
        monthComboBox.setModel(new DefaultComboBoxModel<>(months));

        // Set the selected month if it is within the new range
        if (Integer.parseInt(mesSelecionado) <= nMonths) {
            monthComboBox.setSelectedItem(mesSelecionado);
        }

        // Update days based on the selected month and year
        updateDaysComboBox(dayComboBox, monthComboBox, yearComboBox);
    }

    // Método auxiliar para verificar se o dia é hoje por continuar
    private boolean isToday(int day) {
        LocalDate currentDate = LocalDate.now();
        return day == currentDate.getDayOfMonth();
    }

    
    private void updateHours(boolean start) {
        System.out.println("asdas");
    }

    
    private void updateDays(boolean isStart){
        LocalDate currentDate = LocalDate.now();

        if(isStart){
            boolean sameYear = (currentDate.getYear()== Integer.parseInt(startYearComboBox.getSelectedItem().toString()));
            boolean sameMonth = (currentDate.getMonthValue()== Integer.parseInt(startMonthComboBox.getSelectedItem().toString()));
            int startDay = (sameYear && sameMonth ?  currentDate.getDayOfMonth():1);

            startMonthComboBox.setModel(getNewModel(startDay,12));
            updateHours(isStart);
            
            
            //updateMonths
            updateDays(false);
        }else{
            String startMonth = startMonthComboBox.getSelectedItem().toString();
            String endMonth = endMonthComboBox.getSelectedItem().toString();
            
            int endDay = (startMonth.equals(endMonth) ?  currentDate.getDayOfMonth():1);
            endMonthComboBox.setModel(getNewModel(endDay,12));

        }
    }
    
    
    private void updateMonths(boolean isStart) {
        LocalDate currentDate = LocalDate.now();

        //Mudar os meses para os do novo ano
        //Serve mais para o casso de estar 2023 a metade e passar para 2024
        if(isStart){
            
            int startMonth = (currentDate.getYear()== Integer.parseInt(startYearComboBox.getSelectedItem().toString()) ?  currentDate.getMonthValue():1);

            startMonthComboBox.setModel(getNewModel(startMonth,12));
            updateDays(isStart);
            
            
            //updateMonths
            updateMonths(false);
            
        }else{
            String startYear = startYearComboBox.getSelectedItem().toString();
            String endYear = endYearComboBox.getSelectedItem().toString();
            int baseYear = Integer.parseInt(startYear);
            int endMonth = (startYear.equals(endYear) ?  currentDate.getMonthValue():baseYear);
            endMonthComboBox.setModel(getNewModel(endMonth,12));

        }
        
    }
    
    
    private void updateYears(boolean isStart) {
        updateMonths(isStart);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tpElection = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jButton_NewElection = new javax.swing.JButton();
        jTextField_ElectionName = new javax.swing.JTextField();
        jPanel_candidates = new javax.swing.JPanel();
        jTextField_candidate = new javax.swing.JTextField();
        jButton_addCandidate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_candidates = new javax.swing.JList<>();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel_start = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        startMinComboBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        startMonthComboBox = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        startYearComboBox = new javax.swing.JComboBox<>();
        startDayComboBox = new javax.swing.JComboBox<>();
        startHourComboBox = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jPanel_end = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        endDayComboBox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        endMonthComboBox = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        endYearComboBox = new javax.swing.JComboBox<>();
        endHourComboBox = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        endMinComboBox = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Election App");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tpElection.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tpElectionStateChanged(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton_NewElection.setText("Nova Eleição");
        jButton_NewElection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NewElectionActionPerformed(evt);
            }
        });
        jPanel1.add(jButton_NewElection, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, -1));
        jPanel1.add(jTextField_ElectionName, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 150, -1));

        jPanel_candidates.setBorder(javax.swing.BorderFactory.createTitledBorder("Candidatos"));
        jPanel_candidates.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel_candidates.add(jTextField_candidate, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 60, 180, -1));

        jButton_addCandidate.setText("Add Candidate");
        jButton_addCandidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_addCandidateActionPerformed(evt);
            }
        });
        jPanel_candidates.add(jButton_addCandidate, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 60, -1, -1));

        jScrollPane1.setViewportView(jList_candidates);

        jPanel_candidates.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 50, 210, 180));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Candidato");
        jPanel_candidates.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 180, -1));

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Candidatos");
        jPanel_candidates.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 20, 210, -1));

        jPanel1.add(jPanel_candidates, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 200, 600, 260));

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Nome da Eleição");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 60, 150, -1));

        jPanel_start.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Inicio da Eleição");
        jPanel_start.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 20));

        startMinComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startMinComboBoxItemStateChanged(evt);
            }
        });
        jPanel_start.add(startMinComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 0, 50, -1));

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("/");
        jPanel_start.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 20, 20));

        startMonthComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startMonthComboBoxItemStateChanged(evt);
            }
        });
        startMonthComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMonthComboBoxActionPerformed(evt);
            }
        });
        jPanel_start.add(startMonthComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 50, -1));

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText(":");
        jPanel_start.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 20, 20));

        startYearComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startYearComboBoxItemStateChanged(evt);
            }
        });
        jPanel_start.add(startYearComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 70, -1));

        startDayComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startDayComboBoxItemStateChanged(evt);
            }
        });
        jPanel_start.add(startDayComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 50, -1));

        startHourComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                startHourComboBoxItemStateChanged(evt);
            }
        });
        jPanel_start.add(startHourComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 50, -1));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("/");
        jPanel_start.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 20, 20));

        jPanel1.add(jPanel_start, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 120, 500, 30));

        jPanel_end.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Fim da Eleição");
        jPanel_end.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 20));

        endDayComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                endDayComboBoxItemStateChanged(evt);
            }
        });
        jPanel_end.add(endDayComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 50, -1));

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("/");
        jPanel_end.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 20, 20));

        endMonthComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                endMonthComboBoxItemStateChanged(evt);
            }
        });
        endMonthComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                endMonthComboBoxActionPerformed(evt);
            }
        });
        jPanel_end.add(endMonthComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 0, 50, -1));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("/");
        jPanel_end.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 0, 20, 20));

        endYearComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                endYearComboBoxItemStateChanged(evt);
            }
        });
        jPanel_end.add(endYearComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 0, 70, -1));

        endHourComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                endHourComboBoxItemStateChanged(evt);
            }
        });
        jPanel_end.add(endHourComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 0, 50, -1));

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText(":");
        jPanel_end.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 0, 20, 20));

        endMinComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                endMinComboBoxItemStateChanged(evt);
            }
        });
        jPanel_end.add(endMinComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 0, 50, -1));

        jPanel1.add(jPanel_end, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 490, 30));

        tpElection.addTab("Eleições", jPanel1);

        getContentPane().add(tpElection, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1386, 690));

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void tpElectionStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tpElectionStateChanged
        /*if( tpElection.getSelectedComponent()== pnUsersBalance){
            DefaultListModel model = new DefaultListModel();
            model.addAll(election.getCandidateVotes());
        }*/
    }//GEN-LAST:event_tpElectionStateChanged

    private void startMonthComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startMonthComboBoxItemStateChanged
        updateMonths(true);
    }//GEN-LAST:event_startMonthComboBoxItemStateChanged

    private void startMonthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMonthComboBoxActionPerformed

    }//GEN-LAST:event_startMonthComboBoxActionPerformed

    private void endMonthComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_endMonthComboBoxItemStateChanged
        updateMonths(false);
        
    }//GEN-LAST:event_endMonthComboBoxItemStateChanged

    private void endMonthComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_endMonthComboBoxActionPerformed

    }//GEN-LAST:event_endMonthComboBoxActionPerformed

    private void jButton_addCandidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_addCandidateActionPerformed

        // Retrieve the text from the JTextField
        String candidateName = jTextField_candidate.getText();

        // Add the text to the data model of the JList
        candidatesListModel.addElement(candidateName);

        // Set the data model to the JList
        jList_candidates.setModel(candidatesListModel);

        // Clear the JTextField after adding the candidate
        jTextField_candidate.setText("");
    }//GEN-LAST:event_jButton_addCandidateActionPerformed

    private void startYearComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startYearComboBoxItemStateChanged
        //updateMonthsAndDays(startMonthComboBox, startMinComboBox, startYearComboBox);
        updateYears(true);
    }//GEN-LAST:event_startYearComboBoxItemStateChanged

    private void endYearComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_endYearComboBoxItemStateChanged
        //updateMonthsAndDays(endMonthComboBox, endDayComboBox, endYearComboBox);
        updateYears(false);
    }//GEN-LAST:event_endYearComboBoxItemStateChanged

    private void startMinComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startMinComboBoxItemStateChanged
        //updateHoursAndMinutes(startHourComboBox, startMinComboBox, startDayComboBox, startMonthComboBox, startYearComboBox, true);
    }//GEN-LAST:event_startMinComboBoxItemStateChanged

    private void endMinComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_endMinComboBoxItemStateChanged
        //updateHoursAndMinutes(endHourComboBox, endMinComboBox, endDayComboBox, endMonthComboBox, endYearComboBox, false);
    }//GEN-LAST:event_endMinComboBoxItemStateChanged

    private void startHourComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startHourComboBoxItemStateChanged
        //updateHoursAndMinutes(startHourComboBox, startMinComboBox, startDayComboBox, startMonthComboBox, startYearComboBox, true);
    }//GEN-LAST:event_startHourComboBoxItemStateChanged

    private void endHourComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_endHourComboBoxItemStateChanged
        //updateHoursAndMinutes(endHourComboBox, endMinComboBox, endDayComboBox, endMonthComboBox, endYearComboBox, false);
    }//GEN-LAST:event_endHourComboBoxItemStateChanged

    private void startDayComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_startDayComboBoxItemStateChanged
        
        //updateHoursAndMinutes(startHourComboBox, startMinComboBox, startDayComboBox, startMonthComboBox, startYearComboBox, true);
    }//GEN-LAST:event_startDayComboBoxItemStateChanged

    private void endDayComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_endDayComboBoxItemStateChanged
        //updateHoursAndMinutes(endHourComboBox, endMinComboBox, endDayComboBox, endMonthComboBox, endYearComboBox, false);
    }//GEN-LAST:event_endDayComboBoxItemStateChanged

    private void jButton_NewElectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NewElectionActionPerformed
        // TODO add your handling code here:
        addNewElection();
        
         
    }//GEN-LAST:event_jButton_NewElectionActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> endDayComboBox;
    private javax.swing.JComboBox<String> endHourComboBox;
    private javax.swing.JComboBox<String> endMinComboBox;
    private javax.swing.JComboBox<String> endMonthComboBox;
    private javax.swing.JComboBox<String> endYearComboBox;
    private javax.swing.JButton jButton_NewElection;
    private javax.swing.JButton jButton_addCandidate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList<String> jList_candidates;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel_candidates;
    private javax.swing.JPanel jPanel_end;
    private javax.swing.JPanel jPanel_start;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField_ElectionName;
    private javax.swing.JTextField jTextField_candidate;
    private javax.swing.JComboBox<String> startDayComboBox;
    private javax.swing.JComboBox<String> startHourComboBox;
    private javax.swing.JComboBox<String> startMinComboBox;
    private javax.swing.JComboBox<String> startMonthComboBox;
    private javax.swing.JComboBox<String> startYearComboBox;
    private javax.swing.JTabbedPane tpElection;
    // End of variables declaration//GEN-END:variables

    private void addNewElection() {
//        //Init ElectionCore
        DefaultListModel model = (DefaultListModel) jList_candidates.getModel();
        List<String> candidates = new ArrayList<>();
        for(int i =0; i< model.size(); i++){
            candidates.add(model.getElementAt(i).toString());
        }
        
        //election = new ElectionCore(candidates);
        remote.StartNewElection(jTextField_ElectionName.getText(), candidates);
//        this.user = user;
//        
//        try {
//            ElectionCore.save(fileElection);
//        } catch (Exception ex) {
//            Logger.getLogger(Administration.class.getName()).log(Level.SEVERE, null, ex);
//        }   
//    
    }



}
