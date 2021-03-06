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

import ch.hsr.xclavis.helpers.Base32;
import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gian
 */
public class QRModelTest {

    public QRModelTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test all methods of class QRModel.
     */
    @Test
    public void testGetModell() {
        System.out.println("getModell");
        SessionKey sessionKey128 = new SessionKey(SessionID.SESSION_KEY_128);
        SessionKey sessionKey256 = new SessionKey(SessionID.SESSION_KEY_256);
        ECDHKey ecdhKeyReq256 = new ECDHKey(SessionID.ECDH_REQ_256);
        ECDHKey ecdhKeyReq512 = new ECDHKey(SessionID.ECDH_REQ_512);
        ECDHKey ecdhKeyRes256 = new ECDHKey(SessionID.ECDH_RES_256);
        ECDHKey ecdhKeyRes512 = new ECDHKey(SessionID.ECDH_RES_512);

        System.out.println("Input:");
        System.out.println("ID: " + sessionKey128.getID() + " Key: " + Base32.byteToBase32(sessionKey128.getKey()));
        System.out.println("ID: " + sessionKey256.getID() + " Key: " + Base32.byteToBase32(sessionKey256.getKey()));
        System.out.println("ID: " + ecdhKeyReq256.getID() + " Private Key: " + Base32.byteToBase32(ecdhKeyReq256.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyReq256.getPublicKey()));
        System.out.println("ID: " + ecdhKeyReq512.getID() + " Private Key: " + Base32.byteToBase32(ecdhKeyReq512.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyReq512.getPublicKey()));
        System.out.println("ID: " + ecdhKeyRes256.getID() + " Private Key: " + Base32.byteToBase32(ecdhKeyRes256.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyRes256.getPublicKey()));
        System.out.println("ID: " + ecdhKeyRes512.getID() + " Private Key: " + Base32.byteToBase32(ecdhKeyRes512.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyRes512.getPublicKey()));

        QRModel instance = new QRModel();

        instance.addSessionKey(sessionKey128);
        instance.addSessionKey(sessionKey256);
        instance.addECDHKey(ecdhKeyReq256);
        instance.addECDHKey(ecdhKeyReq512);
        instance.addECDHKey(ecdhKeyRes256);
        instance.addECDHKey(ecdhKeyRes512);

        String modell = instance.getModel();
        System.out.println("Result: " + modell);

        System.out.println("getKeys");
        String[][] keys = instance.getStandardKeys(modell);

        System.out.println("Result:");
        for (int i = 0; i < keys.length; i++) {
            String sessionID = keys[i][0];
            String key = keys[i][1];
            System.out.println("ID: " + sessionID + " Key: " + key);
        }

        assertEquals(sessionKey128.getID(), keys[0][0]);
        assertEquals(sessionKey256.getID(), keys[1][0]);
        assertEquals(ecdhKeyReq256.getID(), keys[2][0]);
        assertEquals(ecdhKeyReq512.getID(), keys[3][0]);
        assertEquals(ecdhKeyRes256.getID(), keys[4][0]);
        assertEquals(ecdhKeyRes512.getID(), keys[5][0]);

        assertEquals(Base32.byteToBase32(sessionKey128.getKey()), keys[0][1]);
        assertEquals(Base32.byteToBase32(sessionKey256.getKey()), keys[1][1]);
        assertEquals(Base32.byteToBase32(ecdhKeyReq256.getPublicKey()), keys[2][1]);
        assertEquals(Base32.byteToBase32(ecdhKeyReq512.getPublicKey()), keys[3][1]);
        assertEquals(Base32.byteToBase32(ecdhKeyRes256.getPublicKey()), keys[4][1]);
        assertEquals(Base32.byteToBase32(ecdhKeyRes512.getPublicKey()), keys[5][1]);
    }
}
