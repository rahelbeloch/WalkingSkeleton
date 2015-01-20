package de.hsrm.swt02.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This class represents an Item. It extends the class RootElement, so it can
 * have an Id.
 * 
 * The metadata list is represented by meta entries, which consist of a key, value and group.
 * key can be a stepid, opener or status. 
 * value can be empty, an opener's name or a state (BUSY, OPEN, INACTIVE, DONE).
 * group can be form, step, or a stepid.
 * 
 * It's structure should resemble a Map with key value pairs:
 * e.g.
 * step {
 *          "1001": {
 *                      "opener":"alex";
 *                      "status":"OPEN";
 *                    }
 *          ...
 *          "1002": {
 *                      "opener":"dominik";
 *                      "status":"INACTIVE";
 *                    }
 *          ...
 *      }
 * form {
 *          "name": "alex";
 *          "alter": 22;
 *          ...
 * }
 */
public class Item extends RootElement {
    
    @JsonIgnore
    private static final long serialVersionUID = 2902617504190652392L;

    // Used for (de)serialization. Do not change.
    private String workflowId;

    // Used for (de)serialization. Do not change.
    private List<MetaEntry> metadata;

    // Used for (de)serialization. Do not change.
    private boolean finished;


    /**
     * Constructor for Item.
     */
    public Item() {
        super();
        metadata = new ArrayList<MetaEntry>();
    }

    /**
     * WorkflowId getter.
     * 
     * @return workflowId id of the workflow
     */
    public String getWorkflowId() {
        return workflowId;
    }

    /**
     * WorkflowId setter.
     * 
     * @param workflowId
     *            id of the workflow
     */
    public void setWorkflowId(String workflowId) {
        this.workflowId = workflowId;
    }

    /**
     * Metadata (list of MetaEntries) getter.
     * 
     * @return metadata list of metadata
     */
    public List<MetaEntry> getMetadata() {
        return this.metadata;
    }

    /**
     * Finished getter.
     * 
     * @return finished true of false wether or not the workflow is finished
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Finished setter.
     * 
     * @param finished
     *            finished true of false wether or not the workflow is finished
     */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * This method gets an entry of the metadata list which suits the group and
     * key parameters.
     * 
     * @param key
     *            is the id of an entry
     * @param group
     *            is the type of an entry
     * @return the suitable entry, else return NULL
     */
    public MetaEntry getEntry(String key, String group) {

        for (MetaEntry a : metadata) {
            if (a.getGroup().equals(group) && a.getKey().equals(key)) {
                return a;
            }
        }
        return null;
    }

    /**
     * This method gets the value of an entry.
     * 
     * @param key
     *            is the id of an entry
     * @param group
     *            is the type of an entry
     * @return the suitable entry value, if the entry was not found return NULL
     */
    public String getEntryValue(String key, String group) {

        final MetaEntry ame = getEntry(key, group);

        if (ame != null) {
            return ame.getValue();
        }
        return null;
    }

    /**
     * This method returns the Metastate of an entry.
     * 
     * @param group
     *            indicates which step is looked for
     * @return the Metastate of the searched entry
     */
    public String getStepState(String group) {

        return MetaState.fromValue(getEntryValue("status", group)).toString();
    }

    /**
     * This method gets a list which contains looked for entries. (via group)
     * 
     * @param group
     *            is the type of the entries which are looked for
     * @return a list of suitable entries
     */
    public List<MetaEntry> getForGroup(String group) {

        final List<MetaEntry> list = new ArrayList<MetaEntry>();

        for (MetaEntry a : metadata) {
            if (a.getGroup().equals(group)) {
                list.add(a);
            }
        }
        return list;
    }
    
    /**
     * This method gets a list which contains looked for entries. (via key)
     * @param key is the id of entries which are looked for
     * @return list of suitable meta entries
     */
    public List<MetaEntry> getForKey(String key) {
        final List<MetaEntry> list = new ArrayList<MetaEntry>();

        for (MetaEntry a : metadata) {
            if (a.getKey().equals(key)) {
                list.add(a);
            }
        }
        return list;
    }

    /**
     * returns the MetaEntry of the current step.
     * 
     * @return me or null
     */
    public MetaEntry getActStep() {
        for (MetaEntry me : getForKey("status")) {
            if ((me.getValue().equals(MetaState.OPEN.toString())) || me.getValue().equals(MetaState.BUSY.toString())) {
                return me;
            }
        }
        return null;
    }
    
    /**
     * This method returns the opener of an entry.
     * @param key indicates which entry is looked for
     * @return opener name
     */
    public String getEntryOpener(String key) {
        String opener = null;
        
        if (getEntry("opener", key ) != null) {
            opener = getEntry("opener", key ).getValue();
        }
        return opener;
    }

    /**
     * This method sets the value of an entry or adds a new entry.
     * 
     * @param key
     *            is the id of an entry
     * @param group
     *            is the type of an entry
     * @param value
     *            represents an entrie's content
     */
    public void set(String key, String group, String value) {

        final MetaEntry ame = getEntry(key, group);

        if (ame != null) {
            ame.setValue(value);
        } else {
            final MetaEntry entry = new MetaEntry();
            entry.setGroup(group);
            entry.setKey(key);
            entry.setValue(value);
            metadata.add(entry);
        }
    }

    /**
     * This method sets specifically the metastate of an step entry.
     * 
     * @param key
     *            is the id of an entry
     * @param value
     *            is the new state of an entry
     */
    public void setStepState(String key, String value) {

        set("status", key, value);
    }

    /**
     * This methods is just for the initial state setting of an step.
     * @param group indicates to which step the looked for entry is referred to
     * @param value
     *            has to be OPEN, for enabling very first Step
     */
    public void setFirstStepState(String group, String value) {

        getEntry("status", group).setValue(value);
    }
    
    /**
     * This methods sets an opener to a process step.
     * @param key indicates which entry is looked for
     * @param opener indicates which opener will be setted
     */
    public void setEntryOpener(String key, String opener) {
        set("opener", key, opener);
    }
    
    /**
     * This methods applies a form from a workflow to an item.
     * @param form which is used by a workflow
     */
    public void applyForm(Form form) {
        set(form.getId(), "form", "");
        for (FormEntry fe : form.getFormDef()) {
            set(fe.getKey(), form.getId(), "");
        }
    }

    /**
     * Init method for cloning process.
     * 
     * @param i
     *            is the action we want to init
     */
    public void init(Item i) {
        super.init(i);
    }

    /**
     * Deep Copy - Cloning method for Items.
     * 
     * @exception CloneNotSupportedException
     *                convention
     * @throws CloneNotSupportedException
     * @return clone is the finished clone
     */
    public Object clone() throws CloneNotSupportedException {
        final Item clone = new Item();
        clone.init(this);
        clone.setFinished(finished);
        clone.setWorkflowId(workflowId);

        for (MetaEntry me : this.metadata) {
            final MetaEntry cloneMe = (MetaEntry) me.clone();
            clone.getMetadata().add(cloneMe);
        }
        return clone;
    }

    @Override
    public String toString() {
        String ret = "";
        ret += "\tId: " + this.id + "\n";
        ret += "\tWorkflow Id: " + this.workflowId + "\n";
        ret += "\tFinished: " + this.finished + "\n";
        for (MetaEntry me : this.metadata) {
            ret += "\t\t---METAENTRY:\n";
            ret += me.toString();
        }

        return ret;
    }
}
