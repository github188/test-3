package nari.SpatialIndex.loader;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import nari.SpatialIndex.geom.CoordinateSequence;
import nari.SpatialIndex.geom.DefaultCoordinateSequence;
import nari.SpatialIndex.geom.DefaultPolygon;
import nari.SpatialIndex.geom.Envelope;
import nari.SpatialIndex.geom.Geometry;
import nari.SpatialIndex.index.Boundary;
import nari.SpatialIndex.index.Grid;
import nari.SpatialIndex.index.GridMapper;
import nari.SpatialIndex.index.StandardGrid;
import nari.SpatialIndex.searcher.IndexLayer;

public class GridIndexWriter extends AbstractIndexWriter {
	
	private final double GRIDWIDTH = 0.01;
	
	private final double GRIDHEIGHT = 0.01;
	
	private final double OX = 0;
	
	private final double OY = 0;
	
	private Disk disk;
	
	public GridIndexWriter(Disk disk){
		this.disk = disk;
	}

	@Override
	protected Grid[] getGrid(IndexLayer layer, Geometry geom,GridMapper mapper) throws Exception {
		Envelope env = geom.getEnvelope();
		
		int startX = (int)((env.getMinX() - OX)/GRIDWIDTH);
		int stopX = (int)((env.getMaxX() - OX)/GRIDWIDTH) + 1;
		int startY = (int)((env.getMinY() - OY)/GRIDHEIGHT);
		int stopY = (int)((env.getMaxY() - OY)/GRIDHEIGHT) + 1;
		
		int col = stopX - startX;
		int row = stopY - startY;
		
		Grid[] grids = new Grid[col*row];
		int k = 0;
		for(int i=0;i<row;i++){
			double ox = OX+(startX+i)*GRIDWIDTH;
			for(int j = 0;j<col;j++){
				double oy = OY+(startY+j)*GRIDHEIGHT;
				long gridId = createGridId(i+startX,j+startY);
//				System.out.println((i+startX)+","+(j+startY));
//				File f = new File(disk.getFile(),String.valueOf(gridId));
//				if(f.exists()){
//					Grid g = new StandardGrid(gridId,ox,oy,GRIDWIDTH,GRIDHEIGHT,disk);
//					grids[k++] = g;
//					mapper.map(gridId, g);
//				}else{
//					
//				}
//				if(!mapper.hasGrid(gridId)){
					Grid g = new StandardGrid(gridId,ox,oy,GRIDWIDTH,GRIDHEIGHT,disk);
					grids[k++] = g;
//					mapper.map(gridId, g);
//				}else{
//					grids[k++] = mapper.findGrid(gridId);
//				}
			}
		}
		
		List<Grid> list = new ArrayList<Grid>();
		
		for(Grid grid:grids){
			Boundary bd = grid.boundary();
			CoordinateSequence pyseq = new DefaultCoordinateSequence(new double[]{bd.getMinX(),bd.getMinY(),bd.getMinX(),bd.getMaxY(),bd.getMaxX(),bd.getMaxY(),bd.getMaxX(),bd.getMinY(),bd.getMinX(),bd.getMinY()});
			Geometry g = new DefaultPolygon(pyseq);
			
			if(geom.intersects(g)){
				if(!grid.isInit()){
					grid.init();
				}
				list.add(grid);
			}
		}
		Grid[] gs = new Grid[list.size()];
		gs = list.toArray(gs);
//		for(Grid g:grids){
//			System.out.println(g.getGridId()+":"+g.boundary().getMinX()+","+g.boundary().getMinY()+","+g.boundary().getMaxX()+","+g.boundary().getMaxY());
//		}
//		System.out.println("---------------------------------------------------------");
		return gs;
	}
	
	private long createGridId(int colId,int rowId){
		byte[] b = new byte[8];
		
		byte[] colByte = getBytes(colId);
		byte[] rowByte = getBytes(rowId);
		for(int i=0;i<4;i++){
			b[i] = colByte[i];
		}
		
		for(int i=4;i<8;i++){
			b[i] = rowByte[i-4];
		}
		return getLong(b);
	}
	
	private long getLong(byte[] bytes) {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.put(bytes, 0, bytes.length);
		buf.flip();
		return buf.getLong();
	}
	
	private byte[] getBytes(int data) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte)(data & 0xFF);
		bytes[1] = (byte)((data & 0xFF00) >> 8);
		bytes[2] = (byte)((data & 0xFF0000) >> 16);
		bytes[3] = (byte)((data & 0xFF000000) >> 24);
		return bytes;
	}
	
}
