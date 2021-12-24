/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.foodyshop.helper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.stage.FileChooser;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author WIN1064
 */
public abstract class FileHelper {

    public static enum Type {
        FILE, DIRECTOTY
    }

    public static void deleteDiretoryAndFiles(String pathDelete) {
        Path path = Paths.get(pathDelete);
        try (Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder()).forEach(pathItem -> {
                try {
                    Files.deleteIfExists(pathItem);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void copyDiretoryAndFiles(String pathFrom, String pathTo) {
        Path pFrom = Paths.get(pathFrom).toAbsolutePath();
        Path pTo = Paths.get(checkFileAndRename(pathFrom, pathTo));
        // copy if paths is file
        try {
            if (Files.isRegularFile(pFrom)) {
                if (!Files.exists(pTo.getParent())) {
                    Files.createDirectories(pTo.getParent());
                }
                Files.copy(pFrom, pTo);
                // đây là file -> k cần xuống copy folder
                return;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        // copy if path is folder
        try (Stream<Path> walk = Files.walk(pFrom)) {
            walk.forEach(path -> {
                // Lấy pần con của thư mục gốc
                String afterFrom = path.toString().substring(pFrom.toString().length());
                // cộng lại ta đc folder cần chuyển
                Path to = Paths.get(pTo.toString().concat(afterFrom));
                try {
                    if (!Files.exists(to.getParent())) {
                        Files.createDirectories(to.getParent());
                    }
                    Files.copy(path, to);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void moveDiretoryAndFiles(String pathFrom, String pathTo) {
        Path pFrom = Paths.get(pathFrom).toAbsolutePath();
        Path pTo = Paths.get(checkFileAndRename(pathFrom, pathTo));
        try {
            if (!Files.exists(pTo.getParent())) {
                Files.createDirectories(pTo.getParent());
            }
            Files.move(pFrom, pTo);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void moveDiretoryAndFiles(String pathFrom, String pathTo, String newName) {
        Path pFrom = Paths.get(pathFrom).toAbsolutePath();
        Path pTo = Paths.get(pathTo).toAbsolutePath();
        Path to = Paths.get(pTo.toString() + "\\" + pFrom.getFileName());
        try {
            if (!Files.exists(to.getParent())) {
                Files.createDirectories(to.getParent());
            }
            Files.move(pFrom, to.resolveSibling(newName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String checkFileAndRename(String pathFrom, String pathTo) {
        Path pFrom = Paths.get(pathFrom).toAbsolutePath();
        Path pTo = Paths.get(pathTo).toAbsolutePath();

        // Lấy folder được chuyển file tới(pathTo) nối với file hay folder cần chuyển tới -> Được newPath
        String newPath = pTo.toString() + "\\" + pFrom.getFileName();
        if (!Files.exists(Paths.get(newPath))) {
            return newPath;
        }
        // Lấy tên folder||file của source
        String fileName = ("\\" + pFrom.getFileName()).replaceAll(" - Copy \\(\\d\\)", "").replaceAll(" - Copy \\(\\d\\d\\)", "");
        // Nếu là root thì k cần pải thêm \\ ở đằng trc
        if (pTo.getNameCount() == 0) {
            fileName = fileName.substring(1);
        }
        // Khởi tạo tên mới
        StringBuilder newFileName = new StringBuilder(fileName);
        int i = 1;
        while (true) {
            // Gán tên mới vào source mới
            newPath = pTo.toString().concat(newFileName.toString());
            // Kiểm tra source mới xem tồn tại chưa?
            if (Files.exists(Paths.get(newPath))) {
                // Tiến hành đổi tên
                newPath = newPath.replaceAll(" - Copy \\(\\d\\)", "").replaceAll(" - Copy \\(\\d\\d\\)", "");
                if (Files.isRegularFile(Paths.get(pathFrom))) {
                    newFileName = new StringBuilder(fileName.substring(0, fileName.lastIndexOf(".")) + " - Copy (" + (i) + ")" + fileName.substring(fileName.lastIndexOf(".")));
                } else {
                    newFileName = new StringBuilder(fileName + " - Copy (" + (i) + ")");
                }
                i++;
            } else {
                // Trả về source mới
                return newPath;
            }
        }
    }

    public static Stream<Path> searchInFolder(String pathSearch, String keyWord) {
        Path pSearch = Paths.get(pathSearch);
        Stream<Path> listPathSearch = null;
        try {
//            FileSystemView fsv = FileSystemView.getFileSystemView();
            listPathSearch = Files.find(pSearch, 100, (path, attr) -> {
                return path.toString().contains(keyWord);
            });
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            return listPathSearch;
        }
    }

    public static void loadObjFile(String filePath, List list) {
        //ObjectInputStream oin = new ObjectInputStream(Files.newInputStream(Paths.get(filePath)));
        try (FileInputStream fin = new FileInputStream(new File(filePath));
                ObjectInputStream oin = new ObjectInputStream(fin);) {
            while (true) {
                list.add(oin.readObject());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            //No handle because read obj not have stop point.
            //Logger.getLogger(FileHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void saveObjFile(String filePath, List list) {
        //ObjectOutputStream oout = new ObjectOutputStream(Files.newOutputStream(Paths.get(filePath)));
        try (FileOutputStream fout = new FileOutputStream(new File(filePath));
                ObjectOutputStream oout = new ObjectOutputStream(fout);) {
            list.forEach(item -> {
                try {
                    oout.writeObject(item);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static StringBuilder getFileContent(String pathFile) throws IOException {
        Path path = Paths.get(pathFile);
        StringBuilder result = new StringBuilder();
        Stream<String> stream = Files.lines(path, StandardCharsets.UTF_8);
        stream.forEach(s -> result.append(s).append("<br>"));
        return result;
    }

    public static void writeFile(String pathFile, byte[] bytes) {
        Path path = Paths.get(pathFile);
        try {
            Files.write(path, bytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void createFile(String path, String name, Enum<Type> type) throws IOException {
        if (type == Type.FILE) {
            Files.createFile(Paths.get(path + "\\" + name));
        } else {
            Files.createDirectory(Paths.get(path + "\\" + name));
        }
    }

    public static void renameFile(String path, String newName, boolean replaceExists) throws IOException {
        Path source = Paths.get(path);
        if (replaceExists) {
            FileHelper.deleteDiretoryAndFiles(source.resolveSibling(newName).toString());
            Files.move(source, source.resolveSibling(newName), StandardCopyOption.REPLACE_EXISTING);
        } else {
            Files.move(source, source.resolveSibling(newName));
        }
    }

    public static void configureFileImageChooser(FileChooser fileChooser) {
        fileChooser.setTitle("Choose image to upload");
        fileChooser.setInitialDirectory(new File("C:\\PerfLogs"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", Arrays.asList("*.jpg", "*.png")),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
    }
}
