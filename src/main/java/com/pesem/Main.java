package com.pesem;

import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.ClassLoaderAsset;
import org.wildfly.swarm.undertow.WARArchive;
//import org.wildfly.swarm.container.Container;

public class Main {
    public static void main(String... args) throws Exception {
//      final Container container = new Container();
        final WARArchive warArchive = ShrinkWrap.create(WARArchive.class);
        warArchive.addAsWebResource(
                new ClassLoaderAsset("index.xhtml", Main.class.getClassLoader()), "index.xhtml");
        warArchive.addAsWebResource(
                new ClassLoaderAsset("payment.xhtml", Main.class.getClassLoader()), "payment.xhtml");
        warArchive.addAsWebResource(
                new ClassLoaderAsset("transactions.xhtml", Main.class.getClassLoader()), "transactions.xhtml");
        warArchive.addAsWebInfResource(
                new ClassLoaderAsset("WEB-INF/web.xml", Main.class.getClassLoader()), "web.xml");
        warArchive.addAllDependencies();
//      container.start().deploy(warArchive);
    }
}
