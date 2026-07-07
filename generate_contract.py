import os
import re

API_DOC_FILE = "API_CONTRACT.md"
BASE_PACKAGE = "src/main/java/com/condominio/condominio_api"
CONTROLLER_DIR = os.path.join(BASE_PACKAGE, "controller")
DTO_REQ_DIR = os.path.join(BASE_PACKAGE, "dto/request")
DTO_RES_DIR = os.path.join(BASE_PACKAGE, "dto/response")

def get_java_files(dir_path):
    if not os.path.exists(dir_path): return []
    return [os.path.join(dir_path, f) for f in os.listdir(dir_path) if f.endswith(".java")]

def parse_class_mapping(content):
    match = re.search(r'@RequestMapping\("([^"]+)"\)', content)
    return match.group(1) if match else ""

def is_jwt_required(controller_content, method_content):
    if "@PreAuthorize" in method_content: return True
    if "permitAll()" in controller_content: return False
    return True

def extract_endpoints(file_path):
    with open(file_path, "r", encoding="utf-8") as f:
        content = f.read()

    base_path = parse_class_mapping(content)
    endpoints = []
    
    # Regex to find endpoint methods
    # e.g., @GetMapping("/algo") or @PostMapping
    pattern = re.compile(
        r'(@(Get|Post|Put|Delete|Patch)Mapping(\("([^"]*)"\))?)[\s\S]*?public\s+ResponseEntity<[^>]+>\s+(\w+)\s*\(([^)]*)\)',
        re.MULTILINE
    )
    
    for match in pattern.finditer(content):
        full_annotation = match.group(0)
        http_method = match.group(2).upper()
        path = match.group(4) or ""
        method_name = match.group(5)
        params = match.group(6)
        
        url = (base_path + path).replace('//', '/')
        
        req_dto = "N/A"
        req_match = re.search(r'@RequestBody\s+(\w+)', params)
        if req_match: req_dto = req_match.group(1)
        
        res_dto = "Void"
        res_match = re.search(r'ResponseEntity<ApiResponse<([^>]+)>>', full_annotation)
        if res_match: res_dto = res_match.group(1)
        
        jwt_req = is_jwt_required(content, full_annotation)
        
        endpoints.append({
            "method": http_method,
            "url": url,
            "jwt": jwt_req,
            "req_dto": req_dto,
            "res_dto": res_dto,
            "method_name": method_name
        })
    return endpoints

def extract_dto_fields(dto_name, is_request=True):
    if dto_name in ["N/A", "Void", "String", "Long", "Integer", "Boolean"]:
        return "{}"
    
    dir_path = DTO_REQ_DIR if is_request else DTO_RES_DIR
    for root, dirs, files in os.walk(os.path.join(BASE_PACKAGE, "dto")):
        if dto_name + ".java" in files:
            with open(os.path.join(root, dto_name + ".java"), "r", encoding="utf-8") as f:
                content = f.read()
                fields = []
                for line in content.split("\n"):
                    match = re.match(r'\s*private\s+(\w+(?:<[^>]+>)?)\s+(\w+);', line)
                    if match:
                        type_, name = match.groups()
                        fields.append(f'  "{name}": "{type_}"')
                if fields:
                    return "{\n" + ",\n".join(fields) + "\n}"
    return "{}"

def main():
    controllers = get_java_files(CONTROLLER_DIR)
    
    with open(API_DOC_FILE, "w", encoding="utf-8") as out:
        out.write("# API Contract - Condominio API\n\n")
        out.write("Este documento es el contrato oficial generado de la API Spring Boot para ser consumido por React, Flutter y Escritorio.\n\n")
        
        for ctrl in controllers:
            endpoints = extract_endpoints(ctrl)
            if not endpoints: continue
            
            ctrl_name = os.path.basename(ctrl).replace(".java", "")
            out.write(f"## {ctrl_name}\n\n")
            
            for ep in endpoints:
                out.write(f"### {ep['method']} {ep['url']}\n\n")
                out.write(f"- **Método HTTP:** `{ep['method']}`\n")
                out.write(f"- **URL:** `{ep['url']}`\n")
                out.write(f"- **Headers requeridos:** `Content-Type: application/json`" + (", `Authorization: Bearer <token>`" if ep['jwt'] else "") + "\n")
                out.write(f"- **Requiere JWT:** {'Sí' if ep['jwt'] else 'No'}\n")
                out.write(f"- **DTO Utilizado (Request):** `{ep['req_dto']}`\n")
                out.write(f"- **DTO Utilizado (Response):** `{ep['res_dto']}`\n")
                
                codes = "200 OK, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 404 Not Found"
                if ep['method'] == 'POST': codes = "201 Created, 400 Bad Request, 401 Unauthorized, 403 Forbidden, 409 Conflict"
                out.write(f"- **Códigos HTTP posibles:** {codes}\n\n")
                
                out.write("#### Request JSON (Ejemplo)\n```json\n")
                out.write(extract_dto_fields(ep['req_dto'], True) + "\n")
                out.write("```\n\n")
                
                out.write("#### Response JSON (Ejemplo)\n```json\n")
                out.write("{\n")
                out.write('  "status": ' + ('201' if ep['method'] == 'POST' else '200') + ',\n')
                out.write('  "message": "Operación exitosa",\n')
                out.write('  "data": ')
                if ep['res_dto'] == "Void":
                    out.write('null\n')
                elif ep['res_dto'].startswith("List<"):
                    inner_dto = ep['res_dto'][5:-1]
                    out.write('[\n' + extract_dto_fields(inner_dto, False).replace('\n', '\n    ') + '\n  ]\n')
                elif ep['res_dto'].startswith("Page<"):
                    inner_dto = ep['res_dto'][5:-1]
                    out.write('{\n    "content": [\n' + extract_dto_fields(inner_dto, False).replace('\n', '\n      ') + '\n    ],\n    "totalElements": 1,\n    "totalPages": 1\n  }\n')
                else:
                    out.write(extract_dto_fields(ep['res_dto'], False).replace('\n', '\n  ') + '\n')
                out.write("}\n```\n\n")

if __name__ == "__main__":
    main()
