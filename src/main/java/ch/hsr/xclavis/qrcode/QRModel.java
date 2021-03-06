/*
 * Copyright (c) 2015, Gian Poltéra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1.	Redistributions of source code must retain the above copyright notice,
 *   	this list of conditions and the following disclaimer.
 * 2.	Redistributions in binary form must reproduce the above copyright 
 *   	notice, this list of conditions and the following disclaimer in the 
 *   	documentation and/or other materials provided with the distribution.
 * 3.	Neither the name of HSR University of Applied Sciences Rapperswil nor 
 * 	the names of its contributors may be used to endorse or promote products
 * 	derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ch.hsr.xclavis.qrcode;

import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.crypto.Checksum;
import ch.hsr.xclavis.helpers.Base32;
import ch.hsr.xclavis.helpers.KeySeparator;
import ch.hsr.xclavis.keys.Key;
import ch.hsr.xclavis.keys.PrivaSphereKey;

/**
 * This class generates from any number of keys a QR model and vice versa.
 *
 * @author Gian Poltéra
 */
public class QRModel {

    private final static int BLOCK_LENGTH = 5;
    private final static int BLOCK_CHECKSUM = 1;
    private final static String TITLE = "XClavis";
    private final static String NEWLINE = "\n";
    //private final static String DELIMITER = "\u007c";
    private final static String DELIMITER = "-";
    private final static String UNICODE_SPACE = "\u0020";
    private final static String UNICODE_LOGO = "\ud83d\udd10";
    private final static String UNICODE_ID = "\ud83c\udd94";
    private final static String UNICODE_KEY = "\uD83D\uDD11";

    // PrivaSphere Key
    private final static String TITLE_PS = "PrivaSphere";
    private final static String UNICODE_PS = "\u2709";
    private final static String UNICODE_CALENDAR = "\uD83D\uDCC5";
    private final static String UNICODE_SENDER = "\uD83D\uDCE4";
    private final static String UNICODE_RECEIVER = "\uD83D\uDCE5";
    private int numKeys;
    private String model;

    /**
     * Creates a new QRModel.
     */
    public QRModel() {
        // Title
        model = UNICODE_LOGO + UNICODE_SPACE + TITLE + UNICODE_SPACE;
        numKeys = 0;
    }

    /**
     * Adds a SessionKey to the QRModel.
     *
     * @param sessionKey the SessionKey to be added
     */
    public void addSessionKey(SessionKey sessionKey) {
        addModel(sessionKey.getID(), sessionKey.getKey());
    }

    /**
     * Adds a ECDHKey to the QRModel.
     *
     * @param ecdhKey the ECDHKey to be added
     */
    public void addECDHKey(ECDHKey ecdhKey) {
        addModel(ecdhKey.getID(), ecdhKey.getPublicKey());
    }

    /**
     * Gets the finished QRModel.
     *
     * @return the QR-Model as String
     */
    public String getModel() {
        System.out.println(model);

        return model;
    }

    /**
     * Checks if a QRCode contains a standard key like a SessionKey or a
     * ECDH-Key
     *
     * @param string
     * @return
     */
    public boolean isStandardKey(String string) {
        return string.contains(UNICODE_LOGO + UNICODE_SPACE + TITLE);
    }

    /**
     * Checks if a QRCode contains a PrivaSphereKey
     *
     * @param string
     * @return
     */
    public boolean isPrivaSphereKey(String string) {
        return string.contains(UNICODE_PS);
    }

    /**
     * Gets the standard keys from a QRModel.
     *
     * @param string the QRModel
     * @return the keys as a 2D string-array
     */
    public String[][] getStandardKeys(String string) {
        String[] splittedKeys = string.split(UNICODE_ID);
        String[][] keys = new String[splittedKeys.length - 1][2];

        for (int i = 1; i < splittedKeys.length; i++) {
            String id = splittedKeys[i].substring(1, 1 + BLOCK_LENGTH);
            String key = splittedKeys[i].substring(10);
            String type = id.substring(0, 1);
            String random = id.substring(1, BLOCK_LENGTH - BLOCK_CHECKSUM);
            SessionID sessionID = new SessionID(type, random);

            // Remove the delemiters and checksums from the key
            String[] blocks = key.split(DELIMITER);
            key = "";
            for (String block : blocks) {
                key += block.substring(0, BLOCK_LENGTH - BLOCK_CHECKSUM);
            }
            int keyLength = sessionID.getKeyLength() + sessionID.getAddCordLength();
            if ((keyLength % Base32.SIZE) == 0) {
                keyLength = keyLength / Base32.SIZE;
            } else {
                keyLength = keyLength / Base32.SIZE + 1;
            }
            key = key.substring(0, keyLength);

            keys[i - 1][0] = sessionID.getID();
            keys[i - 1][1] = key;

            System.out.println(sessionID.getID());
            System.out.println(key);
        }

        return keys;
    }

    /**
     * Gets the PrivaSphere key from a QRModel.
     *
     * @param string the QRModel
     * @return the PrivaSphereKey
     */
    public PrivaSphereKey getPrivaSphereKey(String string) {
        String id = string.split("\\(")[1].split("\\)")[0];
        String key = string.split(UNICODE_KEY)[1].substring(1);
        String date = string.split(UNICODE_CALENDAR)[1].substring(1, 11);
        String partner = string.split(UNICODE_SENDER)[1].substring(1).split(NEWLINE)[0];
        SessionID sessionID = new SessionID(SessionID.PRIVASPHERE_KEY, id);
        PrivaSphereKey privaSphereKey = new PrivaSphereKey(sessionID, key);
        privaSphereKey.setCreationDate(date);
        privaSphereKey.setPartner(partner);
        privaSphereKey.setState(Key.PRIVASPHERE);
        
        System.out.println(sessionID.getID());
        System.out.println(key);

        return privaSphereKey;
    }

    private void addModel(String id, byte[] key) {
        numKeys++;
        // Info Block, TYP 1 Character, ID 3 Character, CHECKSUM 1 Character
        String infoBlock = id;
        String infoChecksum = Checksum.get(infoBlock, BLOCK_CHECKSUM);

        if (numKeys > 1) {
            model += NEWLINE;
        }
        model += UNICODE_ID + UNICODE_SPACE + infoBlock + infoChecksum + NEWLINE;

        // Key Blocks
        model += UNICODE_KEY + UNICODE_SPACE;
        String[] keyBlocks = KeySeparator.getSeparated(Base32.byteToBase32(key), BLOCK_LENGTH - BLOCK_CHECKSUM);
        String lastBlock = "";
        for (String keyBlock : keyBlocks) {
            if (keyBlock.length() == BLOCK_LENGTH - BLOCK_CHECKSUM) {
                String blockChecksum = Checksum.get(keyBlock, BLOCK_CHECKSUM);
                model += keyBlock + blockChecksum + DELIMITER;
            } else {
                lastBlock = keyBlock;
            }
        }
        int overallChecksumLength = BLOCK_LENGTH - BLOCK_CHECKSUM - lastBlock.length();
        String overallChecksum = Checksum.get(Base32.byteToBase32(key), overallChecksumLength);
        String blockChecksum = Checksum.get(lastBlock + overallChecksum, BLOCK_CHECKSUM);
        model += lastBlock + overallChecksum + blockChecksum;
    }
}
