package mychess.entity;

import mychess.util.ReadProperties;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FunctionServer {
    private ServerSocket serverSocket;
    private static String databaseIP;
    private static int databasePort;
    private static String driver = "org.mariadb.jdbc.Driver";
    private static String url;
    private static String databaseUser;
    private static String databasePassword;

    public static void addResult(String name) {
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            Statement statement = conn.createStatement();
            String sql = "update record set record=record+1 where username = \"" + name + "\"";
            System.out.println(sql);
            statement.execute(sql);
            conn.close();
        } catch(ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static int getHistory(String name) {
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            Statement statement = conn.createStatement();
            String sql = "select * from record where username = \"" + name + "\"";
            ResultSet rs = statement.executeQuery(sql);
            int time,i=0;
            rs.last();
            int num = rs.getRow();
            rs.beforeFirst();

            while(rs.next()) {
                time = rs.getInt("record");
                return time;
            }
            conn.close();

        } catch(ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public static String[] getRank() {
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            Statement statement = conn.createStatement();
            String sql = "select * from record order by record asc limit 10";
            ResultSet rs = statement.executeQuery(sql);
            String uname = null;
            int time,i=0;
            rs.last();
            int num = rs.getRow();
            rs.beforeFirst();

            String[] rString = new String[num];
            while(rs.next()) {
                uname = rs.getString("username");
                time = rs.getInt("record");
                rString[i] = String.format("%1$10s\t%2$8s\n", uname,String.valueOf(time));
                i++;
            }
            conn.close();
            return rString;

        } catch(ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();
        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean Verify(String name, String password) {
        boolean verified = false;
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, databaseUser, databasePassword);
            if(!conn.isClosed())
                System.out.println("Succeeded connecting to the Database!");
            Statement statement = conn.createStatement();
            String sql = "select * from user where username = \"" + name + "\" and password = \"" + password + "\"";
            ResultSet rs = statement.executeQuery(sql);
            if (rs.next()) {
                verified = true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return verified;
    }
    public FunctionServer() {
        databaseIP = ReadProperties.DBIP;
        databasePort = Integer.parseInt(ReadProperties.DBPORT);
        databaseUser = ReadProperties.DBUSER;
        databasePassword = ReadProperties.DBPASSWORD;

        url = "jdbc:mariadb://" + databaseIP  + ":" + databasePort + "/chess";
        try{
            //创建Socket
            serverSocket=new ServerSocket(Integer.parseInt(ReadProperties.FUNCTIONPORT));
            while(true){
                Socket socket = serverSocket.accept();
                Thread thread = new Thread(new funcServerThread(socket));
                thread.start();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    class funcServerThread implements Runnable {
        private Socket socket;
        funcServerThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                String tempString = null;
                NormalMessage normalMessage = null;
                while (true) {
                    normalMessage = (NormalMessage) objectInputStream.readObject();
                    tempString = normalMessage.getAttach();
                    switch (tempString) {
                        case "Login": {
                            NormalMessage nameMessage = (NormalMessage)objectInputStream.readObject();
                            String userName = nameMessage.getAttach();
                            NormalMessage passwordMessage = (NormalMessage)objectInputStream.readObject();
                            String password = passwordMessage.getAttach();

                            boolean result = Verify(userName, password);
                            NormalMessage response = new NormalMessage();
                            if (result == true) {
                                response.setAttach("True");
                            }
                            else {
                                response.setAttach("False");
                            }
                            System.out.println("verify result: " + String.valueOf(result));
                            objectOutputStream.writeObject(response);
                            objectOutputStream.flush();
                            break;
                        }
                        case "Rank": {
                            List<String> resultList = Arrays.asList(getRank());
                            NormalMessage resultMessage = new NormalMessage();
                            resultMessage.setAttach(String.valueOf(resultList.size()));
                            objectOutputStream.writeObject(resultMessage);
                            for (int i = 0; i < resultList.size(); i += 1) {
                                NormalMessage dataMessage = new NormalMessage();
                                dataMessage.setAttach(resultList.get(i));
                                objectOutputStream.writeObject(dataMessage);
                            }
                            objectOutputStream.flush();
                            break;
                        }
                        case "History":{
                            NormalMessage nameMessage = (NormalMessage) objectInputStream.readObject();
                            String name = nameMessage.getAttach();
                            System.out.println(name);
                            int wins = getHistory(name);
                            NormalMessage winsMessage = new NormalMessage();
                            winsMessage.setAttach(String.valueOf(wins));
                            objectOutputStream.writeObject(winsMessage);
                            objectOutputStream.flush();
                        }
                    }
                }
            } catch (Exception e) {
                // do nothing
            }
        }
    }
}
