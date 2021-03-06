package kh.com.kshrd.ipcam.controller.camera;

import com.sun.org.apache.xpath.internal.operations.Mod;

import java.io.File;
import java.lang.String;
import kh.com.kshrd.ipcam.entity.camera.Model;
import kh.com.kshrd.ipcam.entity.form.ModelInputer;
import kh.com.kshrd.ipcam.entity.form.ModelModifier;
import kh.com.kshrd.ipcam.respone.*;
import kh.com.kshrd.ipcam.service.impl.ModelServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by rina on 12/21/16.
 */
@Controller
@RequestMapping("api/model")
public class ModelController {

    @Autowired
    ModelServiceImpl modelService;

    @RequestMapping(value = "/getAllModel",
            method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public ResponseObject<Model> getAllModel(){
        ResponseObject<Model> response  = new ResponseObject<>();
        ArrayList<Model> list = modelService.getAllModel();
        ArrayList<Model>  mainList = getListModel(list);
            try{
                if(mainList!=null){
                    response.setCode(ResponseCode.QUERY_FOUND);
                    response.setMessage(ResponseMessage.MODEL_MESSAGE);
                    response.setData(mainList);
                }
                else {
                    response.setCode(ResponseCode.QUERY_NOT_FOUND);
                    response.setMessage(ResponseMessage.MODEL_MESSAGE);
                }
            }
            catch (Exception e){
                response.setCode(ResponseCode.QUERY_NOT_FOUND);
                response.setMessage(e.getMessage());
            }

        return response;
    }

    @RequestMapping(value = "/getModelNameByVenderName",
                    method = RequestMethod.GET,
                   produces = "application/json")
    @ResponseBody
    public ResponseList<Model> getModelNameByVenderName(@RequestParam("VENDER_NAME")String vender_name){
        ResponseList<Model> response = new ResponseList<>();
        ArrayList<Model> listModel = modelService.getAllModelName(vender_name);
        ArrayList<Model> mainList = getListModel(listModel);
        try{
            if(mainList!=null){
                response.setCode(ResponseCode.QUERY_FOUND);
                response.setMessage(ResponseMessage.MODEL_MESSAGE);
                response.setData(mainList);
            }
            else {
                response.setCode(ResponseCode.QUERY_NOT_FOUND);
                response.setMessage(ResponseMessage.MODEL_MESSAGE);
            }
        }
        catch (Exception e){
            response.setCode(ResponseCode.QUERY_NOT_FOUND);
            response.setMessage(e.getMessage());
        }

        return response;
    }



    @RequestMapping(value = "/getModelById", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ResponseObject<Model> getModelById(@RequestParam("model_id")int model_id){
        ResponseObject<Model> response  = new ResponseObject<>();

        try {
            Model model = modelService.getModelById(model_id);

            if(!model.getImage().contains("http")){
                String directory = environment.getProperty("file.getImage.path");
                model.setImage(directory+"/"+model.getImage());
            }

            if(model!=null){
                response.setCode(ResponseCode.QUERY_FOUND);
                response.setMessage(ResponseMessage.MODEL_MESSAGE);
                response.setData(model);
            }
            else {
                response.setCode(ResponseCode.QUERY_NOT_FOUND);
                response.setMessage(ResponseMessage.MODEL_MESSAGE);
            }
        }
        catch (Exception e){
            response.setCode(ResponseCode.QUERY_NOT_FOUND);
            response.setMessage(e.getMessage());
        }

        return response;
    }

    @Autowired
    Environment environment;
    @RequestMapping(value = "/addModel",method=RequestMethod.POST,consumes = "multipart/form-data;charset=UTF-8")
    @ResponseBody
    public Response addModel(@RequestParam("NAME")String name,
                                      @RequestParam("VENDER_ID")int vender_id,
                                      @RequestParam("PLUGIN_ID")int plugin_id,
                                      @RequestParam("STREAM_URL")String stream_url,
                                      @RequestParam("FILE_IMAGE")MultipartFile multipartFile){


        Response response = new Response();

        ModelInputer modelInputer = new ModelInputer();
        modelInputer.setName(name);
        modelInputer.setVender_id(vender_id);
        modelInputer.setImage(fileNameGen(multipartFile.getOriginalFilename()));
        modelInputer.setPlugin_id(plugin_id);
        modelInputer.setStream_url(stream_url);


        Map<String,Object> map = new HashMap<>();

        try{
            if(modelService.save(modelInputer)){

                // Save the file locally
                BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(filepath));
                stream.write(multipartFile.getBytes());
                stream.close();

                response.setCode(ResponseCode.INSERT_SUCCESS);
                response.setMessage(ResponseMessage.MODEL_MESSAGE);
            }
            else {
                response.setCode(ResponseCode.INSERT_FAIL);
                response.setMessage(ResponseMessage.MODEL_MESSAGE);
            }
            return  response;
        }
        catch (Exception e) {
            response.setCode(ResponseCode.INSERT_FAIL);
            response.setMessage(e.getMessage());
            return null;
        }
    }



    @RequestMapping( value = "/updateModel",
                     method = RequestMethod.PUT,
                     produces = "application/json")
    @ResponseBody
    public Response updateModel(@RequestParam("MODEL_ID")int model_id,
                                @RequestParam("NAME")String name,
                                @RequestParam("VENDER_ID")int vender_id,
                                @RequestParam("PLUGIN_ID")int plugin_id,
                                @RequestParam("STREAM_URL")String stream_url,
                                @RequestParam("FILE_IMAGE")MultipartFile multipartFile){

        Response response = new Response();


        ModelModifier modelModifier = new ModelModifier();
            modelModifier.setModel_id(model_id);
            modelModifier.setName(name);
            modelModifier.setVender_id(vender_id);
            modelModifier.setImage(fileNameGen(multipartFile.getOriginalFilename()));
            modelModifier.setPlugin_id(plugin_id);
            modelModifier.setStream_url(stream_url);

        try {
            if(modelService.update(modelModifier)){
            response.setMessage(ResponseCode.UPDATE_SUCCESS);
            response.setCode(ResponseMessage.MODEL_MESSAGE);

              BufferedOutputStream stream =new BufferedOutputStream(new FileOutputStream(filepath));
              stream.write(multipartFile.getBytes());
              stream.close();
             }
             else {
                response.setCode(ResponseMessage.MODEL_MESSAGE);
                response.setMessage(ResponseCode.UPDATE_FAIL);
              }
           } catch (IOException e) {
                    response.setCode(e.getMessage());
                    response.setMessage(ResponseCode.UPDATE_FAIL);
              }
        return response;
    }


    @RequestMapping(value = "/removeModel", method = RequestMethod.DELETE,
                      produces = "application/json")
    @ResponseBody
    public  Response removeModel(@RequestParam("model_id") int model_id){
        Response response = new Response();

        try{
            if(modelService.remove(model_id)){
                response.setMessage(ResponseCode.DELETE_SUCCESS);
                response.setCode(ResponseMessage.MODEL_MESSAGE);
            }
            else {
                response.setCode(ResponseMessage.MODEL_MESSAGE);
                response.setMessage(ResponseCode.DELETE_FAIL);
            }
        }
        catch (Exception e){
            response.setCode(ResponseMessage.MODEL_MESSAGE);
            response.setMessage(e.getMessage());
        }
        return response;
    }

    /**
     *@Method fileNameGen
     * @Return new file Name gen
     */
    String filepath;
    String fileNameGen(String filename){
        String directory = environment.getProperty("file.upload.path");
        String randomFileName ="";
        try {
            File folder = new File(directory);
            if (!folder.exists()) {
                String[] output = filename.split("\\.");//split string specific "."
                randomFileName = UUID.randomUUID() + "." + "jpg";
                randomFileName = randomFileName + "";
                folder.mkdir();
                filepath = Paths.get(directory, randomFileName).toString();
            }else {
                String[] output = filename.split("\\.");//split string specific "."
                randomFileName = UUID.randomUUID() + "." + "jpg";
                randomFileName = randomFileName + "";
                filepath = Paths.get(directory, randomFileName).toString();
            }
        }
        catch(SecurityException se){
            se.printStackTrace();
        }
        return randomFileName;
    }
    /**
     * @Method to get file directory
     */
    String getFilePath(String fileName){
        String directory = environment.getProperty("file.getImage.path");
        System.out.print("images"+directory);
        filepath = Paths.get(directory, fileName).toString();

        return filepath;
    }

    ArrayList<Model> getListModel(ArrayList<Model> defaulList){
        ArrayList<Model> modelist = new ArrayList<Model>();
        for(Model model:defaulList){
            if(!model.getImage().contains("http")){
                String directory = environment.getProperty("file.getImage.path");
                model.setImage(directory+"/"+model.getImage());
                modelist.add(model);
            }
            else {
                modelist.add(model);
            }
        }
        return modelist;
    }
}