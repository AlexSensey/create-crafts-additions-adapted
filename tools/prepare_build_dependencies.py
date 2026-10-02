"""Stage real Minecraft 26.1.2 Create and JEI binaries for this source branch."""
from pathlib import Path
import argparse,hashlib,json,shutil,zipfile
p=argparse.ArgumentParser()
p.add_argument('--create-jar',type=Path,required=True)
p.add_argument('--jei-jar',type=Path,required=True)
args=p.parse_args();root=Path(__file__).resolve().parents[1]
folder=root/'build/dependencies-26.1.2';folder.mkdir(parents=True,exist_ok=True)
shutil.copy2(args.create_jar,folder/'create.jar');shutil.copy2(args.jei_jar,folder/'jei.jar')
with zipfile.ZipFile(args.create_jar) as z:
 for role,fragment in [('catnip','catnip'),('ponder','ponder'),('flywheel','flywheel'),('registrate','Registrate')]:
  names=[n for n in z.namelist() if n.endswith('.jar') and fragment in Path(n).name]
  assert len(names)==1,(role,names)
  (folder/(role+'.jar')).write_bytes(z.read(names[0]))
manifest={f.name:hashlib.sha256(f.read_bytes()).hexdigest() for f in folder.glob('*.jar')}
(folder/'manifest.json').write_text(json.dumps(manifest,indent=2)+'\n')
print('Staged Create, Catnip, Ponder, Flywheel, Registrate and JEI:',folder)
