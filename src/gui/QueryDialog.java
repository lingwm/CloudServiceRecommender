package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import Module.CloudDescription;
import jcolibri.cbrcore.CBRQuery;
import recommender.CloudRecommender;

/**
 * Query dialgo
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class QueryDialog extends JDialog {

    private static final long serialVersionUID = 1L;

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

    public QueryDialog(JFrame parent)
    {
        super(parent,true);
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

        this.setTitle("Configure Query");

        this.getContentPane().setLayout(new BorderLayout());

        /**********************************************************/
        JPanel panel = new JPanel();
        //panel.setLayout(new GridLayout(8,2));
        panel.setLayout(new SpringLayout());

        JLabel label;
        panel.add(label = new JLabel("Attribute"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));
        panel.add(label = new JLabel("Value"));
        label.setFont(label.getFont().deriveFont(Font.BOLD));

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

//		Lay out the panel.
        Utils.makeCompactGrid(panel,
                14, 2, //rows, cols
                6, 6,        //initX, initY
                10, 10);       //xPad, yPad

        JPanel panelAux = new JPanel();
        panelAux.setLayout(new BorderLayout());
        panelAux.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelAux.add(panel,BorderLayout.NORTH);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout());

        JButton ok = new JButton("Set Query >>");
        ok.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                setQuery();
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

    void setQuery()
    {
        this.setVisible(false);
    }

    public CBRQuery getQuery()
    {
        CloudDescription desc = new CloudDescription();
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

        CBRQuery query = new CBRQuery();
        query.setDescription(desc);

        return query;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        QueryDialog qf = new QueryDialog(null);
        qf.setVisible(true);
        System.out.println("Bye");
    }



}
