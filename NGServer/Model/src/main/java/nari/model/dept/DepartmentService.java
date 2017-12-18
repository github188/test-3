package nari.model.dept;

import nari.model.bean.DepartmentDef;

public interface DepartmentService {

	public static final DepartmentService NONE = new DepartmentService(){

		@Override
		public DepartmentDef findDepartment(String deptId) {
			return null;
		}
		
	};
	
	public DepartmentDef findDepartment(String deptId);
	
}
