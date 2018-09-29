package sample.server;

import sample.Admin.Admin;
import sample.Admin.Database.HandlerAdmin;
import sample.User.Database.Handler;
import sample.User.User;
import sample.protocol.Protocol;
import sample.protocol.ProtocolAdmin;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

    public static void main(String[] args) {

        final List<Socket> clients = new ArrayList<Socket>();
        try {

            System.out.println("Server is running...");

            Thread thread = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            ServerSocket serverSocket = new ServerSocket(10000);
                            while (true) {
                                Socket socket = serverSocket.accept();
                                Thread thread1 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            InputStream inputStream = socket.getInputStream();
                                            OutputStream outputStream = socket.getOutputStream();
                                            clients.add(socket);
                                            try {
                                                while (!socket.isClosed()) {
                                                    User user = Protocol.deserialize(inputStream);

                                                    System.out.println("Пришло: " + user.getUserLogin());

                                                    Handler dbHandler = new Handler();
                                                    ResultSet resultSet = dbHandler.getUser(user);

                                                    int counter = 0;

                                                    while (resultSet.next()) {
                                                        counter++;
                                                    }
                                                    if (counter >= 1) {
                                                        System.out.println("такой пользователь есть");
                                                        user.setUserLogin("true");
                                                        user.setUserPassword("true");

                                                        for (Socket client : clients) {
                                                            client.getOutputStream().write(Protocol.serialize(user));
                                                        }

                                                    } else {
                                                        user.setUserLogin("false");
                                                        for (Socket client : clients) {
                                                            client.getOutputStream().write(Protocol.serialize(user));
                                                        }

                                                    }

                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            inputStream.close();
                                            outputStream.close();
                                            socket.close();
                                            //clients.remove(socket);
                                            System.out.println("clients = " + clients.size());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread1.start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread.start();
            Thread thread1 = new Thread() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            ServerSocket serverSocket = new ServerSocket(5000);
                            while (true) {
                                Socket socket = serverSocket.accept();
                                Thread thread11 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            InputStream inputStream = socket.getInputStream();
                                            OutputStream outputStream = socket.getOutputStream();
                                            clients.add(socket);
                                            try {
                                                while (!socket.isClosed()) {
                                                    User user = Protocol.deserialize(inputStream);
                                                    System.out.println(user.getUserLogin());

                                                    Handler dbHandler = new Handler();

                                                    User newUser = new User(user.getUserLogin(), user.getUserPassword());
                                                    dbHandler.SignUpUser(newUser);

                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            inputStream.close();
                                            outputStream.close();
                                            socket.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread11.start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread1.start();

            Thread thread2 = new Thread() {
                @Override
                public void run() {
                    while (true) {
                        try {
                            ServerSocket serverSocket = new ServerSocket(2000);
                            while (true) {
                                Socket socket = serverSocket.accept();
                                Thread thread22 = new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            InputStream inputStream = socket.getInputStream();
                                            OutputStream outputStream = socket.getOutputStream();
                                            clients.add(socket);
                                            try {
                                                while (!socket.isClosed()) {
                                                    Admin admin = ProtocolAdmin.deserializeAdmin(inputStream);

                                                    System.out.println("Пришло: " + admin.getLogin());

                                                    HandlerAdmin dbHandler = new HandlerAdmin();
                                                    ResultSet resultSet = dbHandler.getAdmin(admin);

                                                    int counter = 0;

                                                    while (resultSet.next()) {
                                                        counter++;
                                                    }
                                                    if (counter >= 1) {
                                                        admin.setLogin("true");
                                                        admin.setPassword("true");
                                                        System.out.println("true");

                                                        for (Socket client : clients) {
                                                            client.getOutputStream().write(ProtocolAdmin.serializeAdmin(admin));
                                                        }

                                                    } else {
                                                        admin.setLogin("false");
                                                        for (Socket client : clients) {
                                                            client.getOutputStream().write(ProtocolAdmin.serializeAdmin(admin));
                                                        }

                                                    }

                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            inputStream.close();
                                            outputStream.close();
                                            socket.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                thread22.start();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            thread2.start();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}
