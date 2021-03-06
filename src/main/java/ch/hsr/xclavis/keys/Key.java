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
package ch.hsr.xclavis.keys;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Key, she is the super class of a SessionKey and a
 * ECDHKey.
 *
 * @author Gian Poltéra
 */
public class Key {

    // State
    public final static String USABLE = "0";
    public final static String USED = "1";
    public final static String WAIT = "2";
    public final static String REMOTE = "3";
    public final static String PRIVASPHERE = "99";

    // Last use Property
    public final static String NO_ACTIVITY = "0";
    public final static String ENCRYPTION = "1";
    public final static String DECRYPTION = "2";
    public final static String READING = "3";

    private SessionID sessionID;
    private StringProperty partner, creationDate, lastUseDate, lastActivity, id, state;

    /**
     * Creates a new Key with given SessionID.
     *
     * @param sessionID the SessionID of the key.
     */
    public Key(SessionID sessionID) {
        this.sessionID = sessionID;
        this.id = new SimpleStringProperty(this.sessionID.getID());
        this.partner = new SimpleStringProperty("Self");
        this.creationDate = new SimpleStringProperty(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        this.lastUseDate = new SimpleStringProperty(NO_ACTIVITY);
        this.lastActivity = new SimpleStringProperty(NO_ACTIVITY);

        this.state = new SimpleStringProperty(USABLE);
    }

    /**
     * Gets the SessionID from the key.
     *
     * @return the SessionID
     */
    public SessionID getSessionID() {
        return sessionID;
    }

    /**
     * Gets the ID from the key.
     *
     * @return the ID as a string
     */
    public String getID() {
        return sessionID.getID();
    }

    /**
     * Gets the ID from the key.
     *
     * @return the id as a StringProperty
     */
    public StringProperty idProperty() {
        return id;
    }

    /**
     * Gets the partner for which a key has been created.
     *
     * @return the name of the partner as a string
     */
    public String getPartner() {
        return partner.get();
    }

    /**
     * Gets the partner for which a key has been created.
     *
     * @return the name of the partner as a StringProperty
     */
    public StringProperty partnerProperty() {
        return partner;
    }

    /**
     * Sets the partner for which a key has been created.
     *
     * @param partner the name of the partner
     */
    public void setPartner(String partner) {
        this.partner.set(partner);
    }

    /**
     * Gets the date when a key has been created.
     *
     * @return the date as a string
     */
    public String getCreationDate() {
        return creationDate.get();
    }

    /**
     * Gets the date when a key has been created.
     *
     * @return the date as a StringProperty
     */
    public StringProperty creationDateProperty() {
        return creationDate;
    }

    /**
     * Sets the date when a key has been created.
     *
     * @param creationDate the date when a key has been created
     */
    public void setCreationDate(String creationDate) {
        this.creationDate.set(creationDate);
    }

    /**
     * Gets the date when a key has been last used.
     *
     * @return the date as a string
     */
    public String getLastUseDate() {
        return lastUseDate.get();
    }

    /**
     * Gets the date when a key has been last used.
     *
     * @return the date as a StringProperty
     */
    public StringProperty lastUseDateProperty() {
        return lastUseDate;
    }

    /**
     * Sets the date when a key has been last used.
     *
     * @param lastUseDate the date when a key has been last used
     */
    public void setLastUseDate(String lastUseDate) {
        this.lastUseDate.set(lastUseDate);
    }

    /**
     * Gets the activity for which a key has been last used.
     *
     * @return the activity as a string
     */
    public String getLastActivity() {
        return lastActivity.get();
    }

    /**
     * Gets the activity for which a key has been last used.
     *
     * @return the activity as a StringProperty
     */
    public StringProperty lastActivityProperty() {
        return lastActivity;
    }

    /**
     * Sets the activity for which a key has been last used.
     *
     * @param lastActivity the activity for which a key has been last used
     */
    public void setLastActivity(String lastActivity) {
        this.lastActivity.set(lastActivity);
    }

    /**
     * Gets the state of a key.
     *
     * @return the state as a String
     */
    public String getState() {
        return state.get();
    }

    /**
     * Gets the state of a key.
     *
     * @return the state as a StringProperty
     */
    public StringProperty stateProperty() {
        return state;
    }

    /**
     * Sets the state of a key.
     *
     * @param state the state of a key
     */
    public void setState(String state) {
        this.state.set(state);
    }
}
