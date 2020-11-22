/*
 * The MIT License
 *
 * Copyright 2020 A-Peeling.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.apeeling.jpeeling;

import java.io.*;
import java.util.Properties;
/**
 *
 * @author stophman1
 */


public class PropertiesInterface {
    public static Properties readPropertiesFile(String fileName) throws IOException {
        FileInputStream epicgamers = null;
        Properties prop = null;
        try {
            epicgamers = new FileInputStream(fileName);
            prop = new Properties();
            prop.load(epicgamers);
        } catch (IOException pooploader) {
            pooploader.printStackTrace();
        } finally {
            assert epicgamers != null;
            epicgamers.close();
        }
        return prop;
    }

    public static void ChangePropertiesFile(String fileName, String setting, String value) throws IOException {
        Properties property = readPropertiesFile(fileName);
        //write
        FileOutputStream exit = new FileOutputStream(fileName);
        property.setProperty(setting, value);
        property.store(exit, null);
        exit.close();
    }
}
