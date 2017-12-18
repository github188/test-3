package org.Invoker.rpc.listener;

import org.Invoker.rpc.SPI;
import org.Invoker.rpc.exporter.Exporter;

@SPI
public interface ExporterListener {

	public void exported(Exporter<?> exporter);

	public void unexported(Exporter<?> exporter);
}
