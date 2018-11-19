package gui;

import Module.CloudDescription;
import Module.CloudSolution;
import jcolibri.cbrcore.CBRCase;
import recommender.CloudRecommender;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Revision Dialog
 * @author Lingwei M
 * @version 1.0
 */
public class RevisionDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    JLabel caseId;
    JTextField price;
    JTextField region;
    JTextField CPUPower;
    JTextField CPUCores;
    JTextField memory;
    JTextField storage;
    JTextField bandwidth;
    JTextField IOPerformance;
    JTextField AvgResponseTime;
    JTextField MaxLatency;
    JTextField MaxUsers;
    JTextField availability;
    JTextField tiers;
    JTextField name;

    ArrayList<CBRCase> cases;
    int currentCase;

    public RevisionDialog(JFrame main)
    {
        super(main, true);
        configureFrame();
    }

    private void configureFrame()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e1)
        {
        }

        this.setTitle("Revise Cases");
        this.getContentPane().setLayout(new BorderLayout());

        /**********************************************************/
        JPanel panel = new JPanel();
        //panel.setLayout(new GridLayout(8,2));
        panel.setLayout(new SpringLayout());

        JLabel label;

        panel.add(label = new JLabel("Description"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label = new JLabel());

        panel.add(new JLabel("Price"));
        panel.add(price = new JTextField());

        panel.add(new JLabel("Region"));
        panel.add(region = new JTextField());

        panel.add(new JLabel("CPUPower"));
        panel.add(CPUPower = new JTextField());

        panel.add(new JLabel("CPUCores"));
        panel.add(CPUCores = new JTextField());

        panel.add(new JLabel("Memory"));
        panel.add(memory = new JTextField());

        panel.add(new JLabel("Storage"));
        panel.add(storage = new JTextField());

        panel.add(new JLabel("Bandwidth"));
        panel.add(bandwidth = new JTextField());

        panel.add(new JLabel("IOPerformance"));
        panel.add(IOPerformance = new JTextField());

        panel.add(new JLabel("AvgResponseTime"));
        panel.add(AvgResponseTime = new JTextField());

        panel.add(new JLabel("MaxLatency"));
        panel.add(MaxLatency = new JTextField());

        panel.add(new JLabel("MaxUsers"));
        panel.add(MaxUsers = new JTextField());

        panel.add(new JLabel("Availability"));
        panel.add(availability = new JTextField());

        panel.add(new JLabel("Tiers"));
        panel.add(tiers = new JTextField());


        panel.add(label = new JLabel("Solution"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label = new JLabel());

        panel.add(new JLabel("Name"));
        panel.add(name = new JTextField());

//		Lay out the panel.
        Utils.makeCompactGrid(panel,
                16, 2, //rows, cols
                6, 6,        //initX, initY
                30, 10);       //xPad, yPad

        JPanel casesPanel = new JPanel();
        casesPanel.setLayout(new BorderLayout());
        casesPanel.add(panel, BorderLayout.CENTER);

        JPanel casesIterPanel = new JPanel();
        casesIterPanel.setLayout(new FlowLayout());
        JButton prev = new JButton("<<");
        casesIterPanel.add(prev);
        casesIterPanel.add(caseId = new JLabel("Case id"));
        JButton follow = new JButton(">>");
        casesIterPanel.add(follow);

        prev.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                saveCase();
                currentCase = (currentCase+cases.size()-1) % cases.size();
                showCase();
            }
        });

        follow.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                saveCase();
                currentCase = (currentCase+1) % cases.size();
                showCase();
            }
        });

        casesPanel.add(casesIterPanel, BorderLayout.NORTH);


        JPanel panelAux = new JPanel();
        panelAux.setLayout(new BorderLayout());
        panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelAux.add(casesPanel,BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout());

        JButton ok = new JButton("Set Revisions >>");
        ok.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                saveCase();
                next();
            }
        });
        buttons.add(ok,BorderLayout.CENTER);
        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                try {
                    CloudRecommender.getInstance().postCycle();
                } catch (Exception ex) {
                    org.apache.commons.logging.LogFactory.getLog(CloudRecommender.class).error(ex);
                }
                System.exit(-1);
            }
        });
        buttons.add(exit,BorderLayout.WEST);

        panelAux.add(buttons, BorderLayout.SOUTH);
        this.getContentPane().add(panelAux, BorderLayout.CENTER);

        /**********************************************************/


        this.pack();
        this.setSize(600, this.getHeight());
        this.setResizable(false);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - this.getWidth()) / 2,
                (screenSize.height - this.getHeight()) / 2,
                getWidth(),
                getHeight());
    }

    void next()
    {
        this.setVisible(false);
    }


    public void showCases(Collection<CBRCase> cases)
    {
        this.cases = new ArrayList<CBRCase>(cases);
        currentCase = 0;
        showCase();
    }

    void showCase()
    {
        CBRCase _case = cases.get(currentCase);
        this.caseId.setText(_case.getID().toString()+" ("+(currentCase+1)+"/"+cases.size()+")");

        CloudDescription desc = (CloudDescription) _case.getDescription();

        this.price.setText(desc.getPrice().toString());
        this.region.setText(desc.getRegion());
        this.CPUPower.setText(desc.getCPUPower().toString());
        this.CPUCores.setText(desc.getCPUCores().toString());
        this.memory.setText(desc.getMemory().toString());
        this.storage.setText(desc.getStorage().toString());
        this.bandwidth.setText(desc.getBandwidth().toString());
        this.IOPerformance.setText(desc.getIOPerformance().toString());
        this.AvgResponseTime.setText(desc.getAvgResponseTime().toString());
        this.MaxLatency.setText(desc.getMaxLatency().toString());
        this.MaxUsers.setText(desc.getMaxUsers().toString());
        this.availability.setText(desc.getAvailability().toString());
        this.tiers.setText(desc.getTiers().toString());

        CloudSolution sol = (CloudSolution) _case.getSolution();
        this.name.setText(sol.getName());
    }

    void saveCase()
    {
        CBRCase _case = cases.get(currentCase);
        this.caseId.setText(_case.getID().toString()+" ("+(currentCase+1)+"/"+cases.size()+")");

        CloudDescription desc = (CloudDescription) _case.getDescription();
        desc.setPrice(Integer.valueOf(price.getText()));
        desc.setRegion(region.getText());
        desc.setCPUPower(Double.valueOf(CPUPower.getText()));
        desc.setCPUCores(Integer.valueOf(CPUCores.getText()));
        desc.setMemory(Integer.valueOf(memory.getText()));
        desc.setStorage(Integer.valueOf(storage.getText()));
        desc.setBandwidth(Integer.valueOf(bandwidth.getText()));
        desc.setIOPerformance(Integer.valueOf(IOPerformance.getText()));
        desc.setAvgResponseTime(Integer.valueOf(AvgResponseTime.getText()));
        desc.setMaxLatency(Integer.valueOf(MaxLatency.getText()));
        desc.setMaxUsers(Integer.valueOf(MaxUsers.getText()));
        desc.setAvailability(Integer.valueOf(availability.getText()));
        desc.setTiers(Integer.valueOf(tiers.getText()));

        CloudSolution sol = (CloudSolution) _case.getSolution();
        sol.setName(this.name.getText());

    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        RevisionDialog qf = new RevisionDialog(null);
        qf.setVisible(true);
        System.out.println("Bye");
    }



}
