package org.example.DTO;


import java.util.ArrayList;
import java.util.List;

public class ApiDepartmentDTO implements Comparable<ApiDepartmentDTO> {
    public long deptId;
    public String name;
   public ApiDepartmentDTO(long deptId, String name) {
       this.deptId = deptId;
       this.name = name;
   }
   public long getDeptId() {
       return deptId;
   }
   public String getName() {
       return name;
   }
   @Override
    public boolean equals(Object other) {
       ApiDepartmentDTO apiDepartmentDTO = (ApiDepartmentDTO) other;
       return this.name.equals(apiDepartmentDTO.name);
   }

    @Override
    public int compareTo(ApiDepartmentDTO other) {
        return name.compareTo(other.name);
    }
}
