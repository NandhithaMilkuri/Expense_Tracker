
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.security.MessageDigest;

public class ExpenseTracker {
    static String currentUser = "";
    public static void main(String[] args) {
        login();
    }
    static String hash(String pass) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] b = md.digest(pass.getBytes());
            StringBuilder sb = new StringBuilder();
            for(byte x:b) sb.append(String.format("%02x",x));
            return sb.toString();
        } catch(Exception e){
            return pass;
        }
    }
    static void register() {
        JFrame f = new JFrame("Register");
        f.setSize(300,200);
        f.setLayout(new GridLayout(3,2));
        JTextField u = new JTextField();
        JPasswordField p = new JPasswordField();
        JButton btn = new JButton("Register");
        f.add(new JLabel("Username")); f.add(u);
        f.add(new JLabel("Password")); f.add(p);
        f.add(new JLabel()); f.add(btn);
        btn.addActionListener(e -> {
            String user = u.getText().trim();
            String pass = hash(new String(p.getPassword()).trim());
            try {
                File file = new File("users.txt");
                if(file.exists()){
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String line;
                    while((line=br.readLine())!=null){
                        String[] data=line.split(",");
                        if(data[0].equals(user)){
                            JOptionPane.showMessageDialog(f,"User exists! Login.");
                            br.close();
                            f.dispose();
                            login();
                            return;
                        }
                    }
                    br.close();
                }
                FileWriter fw = new FileWriter("users.txt", true);
                fw.write(user+","+pass+"\n");
                fw.close();

                JOptionPane.showMessageDialog(f,"Registered!");
                f.dispose();
                login();

            } catch(Exception ex){
                JOptionPane.showMessageDialog(f,"Error!");
            }
        });

        f.setVisible(true);
    }

    static void login() {
        JFrame f = new JFrame("Login");
        f.setSize(300,200);
        f.setLayout(new GridLayout(3,2));

        JTextField u = new JTextField();
        JPasswordField p = new JPasswordField();

        JButton login = new JButton("Login");
        JButton reg = new JButton("Register");

        f.add(new JLabel("Username")); f.add(u);
        f.add(new JLabel("Password")); f.add(p);
        f.add(login); f.add(reg);

        login.addActionListener(e -> {
            boolean found=false;

            String user = u.getText().trim();
            String pass = hash(new String(p.getPassword()).trim());

            try {
                BufferedReader br = new BufferedReader(new FileReader("users.txt"));
                String line;

                while((line=br.readLine())!=null){
                    String[] data=line.split(",");
                    if(data.length==2 && data[0].equals(user) && data[1].equals(pass)){
                        found=true;
                        currentUser=user;
                        break;
                    }
                }
                br.close();
            } catch(Exception ex){
                JOptionPane.showMessageDialog(f,"Register first!");
            }

            if(found){
                f.dispose();
                app();
            } else {
                JOptionPane.showMessageDialog(f,"Invalid Login!");
            }
        });

        reg.addActionListener(e -> {
            f.dispose();
            register();
        });

        f.setVisible(true);
    }

    static void app() {
        JFrame frame = new JFrame("Expense Tracker");
        frame.setSize(1000,600);
        frame.setLayout(new BorderLayout());

        JLabel userLabel = new JLabel("User: " + currentUser);
        frame.add(userLabel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Title","Amount","Category","Date","Month"},0);

        JTable table = new JTable(model);
        frame.add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel panel = new JPanel();

        JTextField title = new JTextField(8);
        JTextField amount = new JTextField(5);
        JTextField category = new JTextField(6);
        JTextField date = new JTextField(8);
        JTextField search = new JTextField(6);

        JButton add = new JButton("Add");
        JButton edit = new JButton("Edit");
        JButton del = new JButton("Delete");
        JButton total = new JButton("Total");
        JButton mtotal = new JButton("Month Total");
        JButton save = new JButton("Save");
        JButton load = new JButton("Load");
        JButton searchBtn = new JButton("Search");
        JButton logout = new JButton("Logout");

        panel.add(title); panel.add(amount); panel.add(category); panel.add(date);
        panel.add(add); panel.add(edit); panel.add(del);
        panel.add(total); panel.add(mtotal);
        panel.add(save); panel.add(load);
        panel.add(search); panel.add(searchBtn);
        panel.add(logout);

        frame.add(panel, BorderLayout.SOUTH);

        JLabel result = new JLabel("Result:");
        frame.add(result, BorderLayout.EAST);

        add.addActionListener(e -> {
            try {
                String inputDate = date.getText().trim();

                if (!inputDate.matches("\\d{2}-\\d{2}-\\d{4}")) {
                    JOptionPane.showMessageDialog(frame,"Use dd-MM-yyyy");
                    return;
                }

                String[] p = inputDate.split("-");
                int d = Integer.parseInt(p[0]);
                int m = Integer.parseInt(p[1]);

                if(d<1||d>31||m<1||m>12){
                    JOptionPane.showMessageDialog(frame,"Invalid date!");
                    return;
                }

                double amt = Double.parseDouble(amount.getText());

                model.addRow(new Object[]{
                        title.getText(), amt, category.getText(),
                        inputDate, p[1]+"-"+p[2]
                });

                title.setText(""); amount.setText("");
                category.setText(""); date.setText("");

            } catch(Exception ex){
                JOptionPane.showMessageDialog(frame,"Invalid input!");
            }
        });

        edit.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r==-1) return;

            String[] p = date.getText().split("-");
            if(p.length==3){
                model.setValueAt(title.getText(),r,0);
                model.setValueAt(amount.getText(),r,1);
                model.setValueAt(category.getText(),r,2);
                model.setValueAt(date.getText(),r,3);
                model.setValueAt(p[1]+"-"+p[2],r,4);
            }
        });

        del.addActionListener(e -> {
            int r = table.getSelectedRow();
            if(r!=-1) model.removeRow(r);
        });

        total.addActionListener(e -> {
            double sum=0;
            for(int i=0;i<model.getRowCount();i++){
                sum+=Double.parseDouble(model.getValueAt(i,1).toString());
            }
            result.setText("Total: "+sum);
        });

        mtotal.addActionListener(e -> {
            String m = JOptionPane.showInputDialog("Enter MM-YYYY");
            double sum=0;

            for(int i=0;i<model.getRowCount();i++){
                if(model.getValueAt(i,4).toString().equals(m)){
                    sum+=Double.parseDouble(model.getValueAt(i,1).toString());
                }
            }
            result.setText("Month Total: "+sum);
        });

        searchBtn.addActionListener(e -> {
            String text = search.getText().toLowerCase();

            for(int i=0;i<model.getRowCount();i++){
                if(model.getValueAt(i,0).toString().toLowerCase().contains(text)){
                    table.setRowSelectionInterval(i,i);
                    return;
                }
            }
            JOptionPane.showMessageDialog(frame,"Not Found");
        });

        save.addActionListener(e -> {
            try{
                FileWriter fw = new FileWriter(currentUser+"_expenses.csv");

                for(int i=0;i<model.getRowCount();i++){
                    fw.write(
                            model.getValueAt(i,0)+","+
                            model.getValueAt(i,1)+","+
                            model.getValueAt(i,2)+","+
                            model.getValueAt(i,3)+","+
                            model.getValueAt(i,4)+"\n"
                    );
                }

                fw.close();
                JOptionPane.showMessageDialog(frame,"Saved!");
            } catch(Exception ex){
                JOptionPane.showMessageDialog(frame,"Error!");
            }
        });

        load.addActionListener(e -> {
            try{
                BufferedReader br = new BufferedReader(
                        new FileReader(currentUser+"_expenses.csv"));

                model.setRowCount(0);
                String line;

                while((line=br.readLine())!=null){
                    model.addRow(line.split(","));
                }

                br.close();
            } catch(Exception ex){
                JOptionPane.showMessageDialog(frame,"No data found!");
            }
        });

        logout.addActionListener(e -> {
            frame.dispose();
            currentUser="";
            login();
        });

        frame.setVisible(true);
    }
}
