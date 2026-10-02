package createadditionprobe;
import java.nio.file.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import com.mrh0.createaddition.index.CABlocks;
import com.mrh0.createaddition.index.CARecipes;
import com.mrh0.createaddition.energy.EnergyCompat;
@Mod("createaddition_probe")
public class CreateAdditionDedicatedProbe {
 public CreateAdditionDedicatedProbe(){NeoForge.EVENT_BUS.addListener(this::started);}
 void started(ServerStartedEvent e){
  try {
   var server=e.getServer();var level=server.overworld();int blocks=0,entities=0;
   for(var entry:BuiltInRegistries.BLOCK.entrySet()){
    if(!entry.getKey().identifier().getNamespace().equals("createaddition"))continue;
    var pos=new BlockPos(blocks*3,100,0);level.getChunkAt(pos);var state=entry.getValue().defaultBlockState();
    level.setBlock(pos,state,2);
    if(state.hasBlockEntity()){
     if(level.getBlockEntity(pos)==null)throw new AssertionError("Missing block entity: "+entry.getKey());
     entities++;
    }
    blocks++;
   }
   if(blocks<15||entities<8)throw new AssertionError("Insufficient registered content");
   var manager=server.getRecipeManager();StringBuilder result=new StringBuilder("PASS dedicated server registered and placed "+blocks+" blocks, including "+entities+" block entities.\n");
   for(var type:java.util.List.of(CARecipes.ROLLING_TYPE.get(),CARecipes.CHARGING_TYPE.get(),CARecipes.LIQUID_BURNING_TYPE.get())){
    long count=manager.getRecipes().stream().filter(r->r.value().getType()==type).count();
    if(count==0)throw new AssertionError("No recipes: "+type);result.append("PASS "+type+" recipes: "+count+"\n");
   }
   var motorPos=new BlockPos(0,100,5);level.setBlock(motorPos,CABlocks.ELECTRIC_MOTOR.getDefaultState(),2);
   var energy=EnergyCompat.legacy(level.getCapability(Capabilities.Energy.BLOCK,motorPos,Direction.UP));
   if(energy==null||energy.receiveEnergy(1000,false)<=0||energy.getEnergyStored()<=0)throw new AssertionError("Electric motor energy capability failed");
   result.append("PASS electric motor accepts and stores energy through native NeoForge capability.\n");
   Files.writeString(Path.of("CREATEADDITION-PASS.txt"),result);
  } catch(Throwable error){error.printStackTrace();try{Files.writeString(Path.of("CREATEADDITION-FAIL.txt"),error.toString());}catch(Exception ignored){}}
  e.getServer().halt(false);
 }
}
