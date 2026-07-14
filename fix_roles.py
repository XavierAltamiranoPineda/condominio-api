import os, glob, re

path = "/home/xavo/GitHub/Personal/condominio-api/src/main/java/com/condominio/condominio_api/controller/**/*.java"

for filepath in glob.glob(path, recursive=True):
    with open(filepath, 'r', encoding='utf-8') as f:
        content = f.read()
    
    def replacer(match):
        full = match.group(0)
        if "hasRole('ADMIN')" in full:
            return full
        if "hasAuthority('ADMIN')" in full:
            full = full.replace("hasAuthority('ADMIN')", "hasRole('ADMIN')")
            # If it only had hasRole('ADMIN'), we don't need to add anything else.
            return full
        
        # Add hasRole('ADMIN') or before the first hasAuthority
        return full.replace('@PreAuthorize("hasAuthority', '@PreAuthorize("hasRole(\'ADMIN\') or hasAuthority')

    new_content = re.sub(r'@PreAuthorize\(".*?"\)', replacer, content)
    
    if new_content != content:
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(new_content)
        print(f"Updated {filepath}")
