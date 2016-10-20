package com.danylo.spokdbupdate;

import java.io.File;
import java.io.IOException;

public class PreparedDataCheck {
    public static boolean check(String path) throws IOException {
        File file = new File(path);
        if (!file.isDirectory()) {
            throw new IOException("incorrect folder path");
        }

        file = new File(path, "categories");
        if(!file.isFile()) {
            throw new IOException("missing \"categories\" file");
        }

        file = new File(path, "users");
        if(!file.isFile()) {
            throw new IOException("missing \"users\" file");
        }

        file = new File(path, "lots");
        if(!file.isFile()) {
            throw new IOException("missing \"lots\" file");
        }
        return true;
    }
}
