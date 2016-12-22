package com.jiam365.modules.persistent;

import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.ArrayList;
import java.util.List;

public class DatastoreFactory
        extends AbstractFactoryBean<Datastore> {
    private Morphia morphia;
    private MongoClient mongoClient;
    private String dbName;
    private List<String> packages = new ArrayList<String>();
    private List<String> mapClasses = new ArrayList<String>();
    private boolean ensureIndexes = true;
    private boolean ensureCaps = true;

    protected Datastore createInstance() throws Exception {
        Datastore ds = this.morphia.createDatastore(this.mongoClient, this.dbName);
        this.packages.forEach(this.morphia::mapPackage);
        for (String entityClass : this.mapClasses) {
            this.morphia.map(new Class[]{Class.forName(entityClass)});
        }
        try {
            if (this.ensureIndexes) {
                ds.ensureIndexes();
            }
            if (this.ensureCaps) {
                ds.ensureCaps();
            }
        }
        catch (MongoException ex) {
            // empty catch block
        }
        return ds;
    }

    public Class<?> getObjectType() {
        return Datastore.class;
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (this.mongoClient == null) {
            throw new IllegalStateException("mongoClient is not set");
        }
        if (this.morphia == null) {
            throw new IllegalStateException("morphia is not set");
        }
    }

    public void setMorphia(Morphia morphia) {
        this.morphia = morphia;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setEnsureIndexes(boolean ensureIndexes) {
        this.ensureIndexes = ensureIndexes;
    }

    public void setEnsureCaps(boolean ensureCaps) {
        this.ensureCaps = ensureCaps;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public void setMapClasses(List<String> mapClasses) {
        this.mapClasses = mapClasses;
    }
}