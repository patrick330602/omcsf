/*
 *   Copyright 2006 University of Dundee. All rights reserved.
 *   Use is subject to license terms supplied in LICENSE.txt
 */
package ome.io.nio.utests;


import java.nio.ByteBuffer;

import org.testng.Assert;
import org.testng.annotations.Test;

import ome.io.nio.RomioPixelBuffer;
import ome.model.core.Pixels;
import ome.model.enums.PixelsType;
import ome.util.checksum.ChecksumProviderFactory;
import ome.util.checksum.ChecksumProviderFactoryImpl;
import ome.util.checksum.ChecksumType;

/**
 * This tests planar and sub-planar data retrieval using a known good ROMIO
 * binary repository data file.
 * 
 * @author Chris Allan <callan@glencoesoftware.com>
 *
 */
public class PlanarDataTest
{
	/** Imported tinyTest.d3d.dv binary repository file. */
	private static final String PIXELS_PATH = "/OMERO/Pixels/100";

	/** Imported tinyTest.d3d.dv pixels ID. */
	private static final long PIXELS_ID = 100L;

	private final ChecksumProviderFactory cpf = new ChecksumProviderFactoryImpl();

	private RomioPixelBuffer getRomioPixelBuffer()
	{
		PixelsType pType = new PixelsType();
		pType.setId(1L);
		pType.setValue("int16");
		Pixels p = new Pixels();
		p.setId(PIXELS_ID);
		p.setSizeX(20);
		p.setSizeY(20);
		p.setSizeZ(5);
		p.setSizeC(1);
		p.setSizeT(6);
		p.setPixelsType(pType);
		
    	RomioPixelBuffer buffer = new RomioPixelBuffer(PIXELS_PATH, p);
    	return buffer;
	}

	@Test(groups={"manual"})
    public void testFirstPlaneSecondTimepointFirstChannelMd5()
		throws Exception
    {
    	RomioPixelBuffer buffer = getRomioPixelBuffer();
    	ByteBuffer buf = buffer.getPlane(0, 0, 1).getData();
    	String md = cpf.getProvider(ChecksumType.MD5).putBytes(buf).checksumAsString();
    	Assert.assertEquals(md, "2d1c16c02bece26920ff04ff08985f5e");
    }

	@Test(groups={"manual"})
    public void testFirstPlaneSecondTimepointFirstChannelFirstEightPixelsMd5()
		throws Exception
    {
    	RomioPixelBuffer buffer = getRomioPixelBuffer();
    	byte[] buf = new byte[16];
    	buffer.getPlaneRegionDirect(0, 0, 1, 8, 0, buf);
    	String md = cpf.getProvider(ChecksumType.MD5).putBytes(buf).checksumAsString();
    	Assert.assertEquals(md, "505c12f3149129adf250ae96af159ea1");
    }

	@Test(groups={"manual"})
    public void testFirstPlaneSecondTimepointFirstChannelSecondEightPixelsMd5()
		throws Exception
    {
    	RomioPixelBuffer buffer = getRomioPixelBuffer();
    	byte[] buf = new byte[16];
    	buffer.getPlaneRegionDirect(0, 0, 1, 8, 8, buf);
    	String md = cpf.getProvider(ChecksumType.MD5).putBytes(buf).checksumAsString();
    	Assert.assertEquals(md, "ed6a8ba38c61808d5790419c7a33839c");
    }

	@Test(groups={"manual"})
    public void testFirstPlaneSecondTimepointFirstChannelLastEightPixelsMd5()
		throws Exception
    {
    	RomioPixelBuffer buffer = getRomioPixelBuffer();
    	byte[] buf = new byte[16];
    	buffer.getPlaneRegionDirect(0, 0, 1, 8, 392, buf);
    	String md = cpf.getProvider(ChecksumType.MD5).putBytes(buf).checksumAsString();
    	Assert.assertEquals(md, "ab1786af4395c09f52de23d710e37a7f");
    }
}
