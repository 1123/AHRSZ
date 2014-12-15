package org.ahrsz.performance;

import org.apache.velocity.app.VelocityEngine;

import java.util.Properties;

public class Context {

    private static VelocityEngine velocityEngine;

    static {
        velocityEngine = new VelocityEngine();
        Properties p = new Properties();
        velocityEngine.init(p);
    }

    public static VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

}
