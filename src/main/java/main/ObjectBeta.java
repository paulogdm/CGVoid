package main;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.core.Material;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.SolidSphere;
import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import br.usp.icmc.vicg.gl.model.Sphere;
import static com.sun.java.accessibility.util.AWTEventMonitor.*;
import java.io.IOException;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;
import javax.swing.JFrame;
import static java.lang.Math.abs;
import static java.lang.Thread.sleep;
import java.util.Random;

public class ObjectBeta implements GLEventListener {
    
    private Shader shader;
    private Matrix4 modelMatrix;//Matrix4 é implementacao da primeira e da segunda prova
    private Matrix4 projectionMatrix;
    private Light light;
    private Material material;
    private boolean go;
    
    //variaveis para camera
    private Camera cam;
    private float speed_x, speed_y, speed_z;
    private float rotate_x, rotate_y, rotate_z;
    private float radius, phi, theta;
    
    private SimpleObject planet;
    
    private MainShip main_ship;
    private Planet moon, earth;
    private MainShip landingShip;
    //private Point[] points_stars;
    private Point[] points_stars;
    //private SolidSphere asteroid;
    private Asteroid asteroid;
    
//    private ship view_ship;
    
    private final InputKey input;
    
    private float left_right_angle;
    private float up_down_angle;
    
    private Timer timer;
    private boolean close_asteroid;
    private boolean close_xwing;
    
    @SuppressWarnings("empty-statement")
    public ObjectBeta() {

        shader = ShaderFactory.getInstance(ShaderFactory.ShaderType.COMPLETE_SHADER);
        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        cam = new Camera();
        //viewMatrix = new Matrix4();
        //viewMatrix_stored = new float[]{0, 0, -1, 0, 0, 0, 0, 1, 0};
        /*viewMatrix_stored = new float[][]{
            {0,2,2},
            {1,5,1},
            {0, 1,0}
        };*/
        
        light = new Light();
        material = new Material();
        
        main_ship = new MainShip();
        
        moon = new Planet("./data/earth/Moon/moon.obj");
        earth = new Planet("./data/earth/Earth/moon.obj");
        //earth = new Planet(".data/earth/death/dearth-star-II.obj");
        //earth= new Planet("./data/earth/death/moon.obj");
        
        points_stars = new Point[5000];
        asteroid = new Asteroid("./data/rock/Rock/Rock.obj");
        
        landingShip = new MainShip();
        
        input = new InputKey();
        
        left_right_angle = 270;
        up_down_angle = 90;
        
        //inicializa variaveis da camera
        speed_x = speed_y = speed_z = 0.0f;
        rotate_x = rotate_y = rotate_z = 0.0f;
        theta = 45f;
        phi = 45f;
        radius = 1.4142f;
        
        timer = new Timer();
        close_asteroid = false;
        close_xwing = false;
    }
   
    @Override
    //quando ele começar a desenhar ele vai setar o init
    public void init(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();//
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glClearDepth(1.0f);
        
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        
        shader.init(gl);
        shader.bind();//indica que shader esta ativo   
        
        modelMatrix.init(gl,shader.getUniformLocation("u_modelMatrix"));
        projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
        //viewMatrix.init(gl, shader.getUniformLocation("u_viewMatrix"));
        cam.init(gl, shader.getUniformLocation("u_viewMatrix"));
        this.go = false;
        try {
            
            main_ship.getObj().getReady(gl, shader);
            main_ship.getObj().addPosition(1.2f, 5.5f, -3.0f);
            main_ship.getObj().addRotation(0, -90.0f, 0);
            
            this.landingShip.getObj().getReady(gl, shader);
            this.landingShip.getObj().setPosition(2.5f,5.5f, -3.0f);
            this.landingShip.getObj().addRotation(0, 90.0f, 0);
            
            
            moon.getObj().getReady(gl, shader);
            moon.changePosition(2.0f, 4.0f, -3.0f);
            //this.moon.getObj().addSize(-0.5f, -0.5f, -0.5f);
            earth.getObj().getReady(gl, shader);
            earth.changePosition(0.0f, 0.0f, -8.0f);
            earth.getObj().addRotation(0, 180, 0);
            this.earth.getObj().addSize(2, 2, 2);
            main_ship.getMissileObj().getReady(gl, shader);
            main_ship.getMissileObj().addPosition(1.36f, 5.48f, -3.29f);
            main_ship.getMissileObj().addRotation(0.0f, 180, 0.0f);
            
            asteroid.getObj().getReady(gl, shader);
            asteroid.getObj().addPosition(0.0f, 10.0f, 0.0f);
            
            this.create_stars(gl);

        } catch (IOException ex) {
            Logger.getLogger(ObjectBeta.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //init the light
        light.init(gl, shader);
        light.setPosition(new float[]{10, 10, 50, 1.0f});
        light.setAmbientColor(new float[]{0.1f, 0.1f, 0.1f, 1.0f});
        light.setDiffuseColor(new float[]{0.75f, 0.75f, 0.75f, 1.0f});
        light.setSpecularColor(new float[]{0.7f, 0.7f, 0.7f, 1.0f});
        light.bind();
        material.init(gl, shader);
        

    }
    
    private void create_stars(GL3 gl){
        Random random = new Random();

        for(int i=0; i < this.points_stars.length; i++){            
            float pointx=0.0f, pointy=0.0f, pointz=0.0f;
            while((pointx < 7.0f && pointx > -7.0f) && (pointy < 7.0f && pointy > -7.0f) && (pointz < 7.0f && pointz > -7.0f) ){
                pointx = (random.nextFloat()-0.5f)*20;
                pointy = (random.nextFloat()-0.5f)*20;
                pointz = (random.nextFloat()-0.5f)*20;
            }
            this.points_stars[i] = new Point(pointx, pointy, pointz, gl);
            this.points_stars[i].init(gl, shader);
        }

    }
    @Override
    public void display(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();//pega gl3 pq pega todas as capacidades de mexer no shader
        
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        
        gl.glClearDepth(1.0f);
        // VARIABLE UPDATES ---------------
       
        projectionMatrix.loadIdentity();
        projectionMatrix.perspective(70.0f, 1f, 0.01f, 30.0f);
        projectionMatrix.bind();
        
        cam.useView();
        light.bind();
        this.playVideo();
        System.out.println(this.timer.getDelta());
        this.cameraUpdate();
       
        this.sceneUpdate();
        
        gl.glFlush();
        
        if(this.input.getExit()){
            this.dispose(glad);
        }
        
    }
    
    
    @Override
    public void dispose(GLAutoDrawable glad) {
        moon.getObj().dispose();
        earth.getObj().dispose();
        main_ship.getObj().dispose();
        main_ship.getMissileObj().dispose();
        landingShip.getObj().dispose();
        for(int i=0; i<this.points_stars.length;i++ ){
            this.points_stars[i].dispose();
        }
        
        this.asteroid.getObj().dispose();
    }
    

    @Override
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        
    }
    
    public void sceneUpdate(){
        
       /*if(this.input.getSpaceBar() && main_ship.getMissileFlag()){
            main_ship.toogleMissileFlag();
        }*/
        
        //main_ship.shoot();
        for(int i=0;i<this.points_stars.length; i++){
            /* RED
            material.setAmbientColor(new float[]{0.5f, 0.5f, 0.5f, 0.0f});
            material.setDiffuseColor(new float[]{1.0f, 0.0f, 0.0f, 0.0f});
            material.setSpecularColor(new float[]{0.9f, 0.9f, 0.9f, 0.0f});
            material.setSpecularExponent(32);
            material.bind();*/
            modelMatrix.loadIdentity();
            modelMatrix.translate(this.points_stars[i].getX(), this.points_stars[i].getY(), this.points_stars[i].getZ());
            modelMatrix.bind();
            this.points_stars[i].bind();
            this.points_stars[i].draw();
        }
        
        moon.getObj().addRotation(0.0f, 0.07f, 0.0f);
        modelMatrix.loadIdentity();
        modelMatrix.translate(moon.getObj().getPosition()[0], moon.getObj().getPosition()[1], moon.getObj().getPosition()[2]);
        modelMatrix.rotate(moon.getObj().getRotation()[0],1,0,0);
        modelMatrix.rotate(moon.getObj().getRotation()[1],0,1,0);
        modelMatrix.rotate(moon.getObj().getRotation()[2],0,0,1);
        modelMatrix.scale(moon.getObj().getSize()[0], moon.getObj().getSize()[1], moon.getObj().getSize()[2]);
        modelMatrix.bind();
        moon.getObj().draw();
        
        earth.getObj().addRotation(0.0f, 0.03f, 0.0f);
        modelMatrix.loadIdentity();
        modelMatrix.translate(earth.getObj().getPosition()[0], earth.getObj().getPosition()[1], earth.getObj().getPosition()[2]);
        modelMatrix.rotate(earth.getObj().getRotation()[0],1,0,0);
        modelMatrix.rotate(earth.getObj().getRotation()[1],0,1,0);
        modelMatrix.rotate(earth.getObj().getRotation()[2],0,0,1);
        modelMatrix.scale(earth.getObj().getSize()[0], earth.getObj().getSize()[1], earth.getObj().getSize()[2]);
        modelMatrix.bind();
        earth.getObj().draw();
        
        modelMatrix.loadIdentity();
        modelMatrix.translate(main_ship.getObj().getPosition()[0], main_ship.getObj().getPosition()[1], main_ship.getObj().getPosition()[2]);
        modelMatrix.rotate(main_ship.getObj().getRotation()[0],1,0,0);
        modelMatrix.rotate(main_ship.getObj().getRotation()[1],0,1,0);
        modelMatrix.rotate(main_ship.getObj().getRotation()[2],0,0,1);
        modelMatrix.scale(main_ship.getObj().getSize()[0], main_ship.getObj().getSize()[1], main_ship.getObj().getSize()[2]);
        modelMatrix.bind();
        main_ship.getObj().draw();
        
        if(!this.close_xwing){
            modelMatrix.loadIdentity();
            modelMatrix.translate(main_ship.getMissileObj().getPosition()[0], main_ship.getMissileObj().getPosition()[1], main_ship.getMissileObj().getPosition()[2]);
            modelMatrix.rotate(main_ship.getMissileObj().getRotation()[0],1,0,0);
            modelMatrix.rotate(main_ship.getMissileObj().getRotation()[1],0,1,0);
            modelMatrix.rotate(main_ship.getMissileObj().getRotation()[2],0,0,1);
            modelMatrix.scale(main_ship.getMissileObj().getSize()[0], main_ship.getMissileObj().getSize()[1], main_ship.getMissileObj().getSize()[2]);
            modelMatrix.bind();
            main_ship.getMissileObj().draw();


            modelMatrix.loadIdentity();
            modelMatrix.translate(landingShip.getObj().getPosition()[0], landingShip.getObj().getPosition()[1], landingShip.getObj().getPosition()[2]);
            modelMatrix.rotate(landingShip.getObj().getRotation()[0],1,0,0);
            modelMatrix.rotate(landingShip.getObj().getRotation()[1],0,1,0);
            modelMatrix.rotate(landingShip.getObj().getRotation()[2],0,0,1);
            modelMatrix.scale(landingShip.getObj().getSize()[0], landingShip.getObj().getSize()[1], landingShip.getObj().getSize()[2]);
            modelMatrix.bind();
            this.landingShip.getObj().draw();
        }
        
        if(!this.close_asteroid){
            material.setAmbientColor(new float[]{0.5f, 0.5f, 0.5f, 0.0f});
            material.setDiffuseColor(new float[]{1.0f, 0.0f, 0.0f, 0.0f});
            material.setSpecularColor(new float[]{0.0f, 0.0f, 0.0f, 0.0f});
            material.setSpecularExponent(32);
            material.bind();
            modelMatrix.loadIdentity();
            modelMatrix.translate(asteroid.getObj().getPosition()[0], asteroid.getObj().getPosition()[1], asteroid.getObj().getPosition()[2]);
            modelMatrix.rotate(asteroid.getObj().getRotation()[0],1,0,0);
            modelMatrix.rotate(asteroid.getObj().getRotation()[1],0,1,0);
            modelMatrix.rotate(asteroid.getObj().getRotation()[2],0,0,1);
            modelMatrix.scale(asteroid.getObj().getSize()[0], asteroid.getObj().getSize()[1], asteroid.getObj().getSize()[2]);
            modelMatrix.bind();
            this.asteroid.getObj().draw();
        }
                
        
        modelMatrix.loadIdentity();
        modelMatrix.bind();
    }
    
    public void userInput(){
    
         if(this.input.getDown()){
            System.out.println("DOWN");
            main_ship.rotate(8.0f, 0, 0);
        }
        
        if(this.input.getUp()){
            System.out.println("UP");
            main_ship.rotate(-8.0f, 0, 0);
        }
        
        if(this.input.getRight()){
            System.out.println("RIGHT");
            main_ship.rotate(0, 8.0f, 0);
        }
        
        if(this.input.getLeft()){
            System.out.println("LEFT");
            main_ship.rotate(0, -8.0f, 0);
        }
        
        if(this.input.getG()){
            this.go = !this.go;
            System.out.println("Hha");
            //ERROR -- PEGA MAIS DE UMA VEZ EM UM UNICO CLIQUE!!!!!!!!!!!
        }
    }
    
    public void setKeyPressed(){
        float speed = 0.1f;
        float rotate = 1.5f;
        if(this.input.getArrowUp()){
            cam.move(speed);
        }else if(this.input.getArrowDown()){
            cam.move(-speed);
        }else if(this.input.getArrowLeft()){
            cam.rotateY(-rotate);
        }else if(this.input.getArrowRight()){
            cam.rotateY(rotate);
        }else if(this.input.getSpaceBar()){
            cam.up(-speed);
        }else if(this.input.getCtrl()){
            cam.up(speed);
        }else if(this.input.getS()){
            cam.lookDown(rotate);
        }else if(this.input.getW()){
            cam.lookDown(-rotate);
        }/*else if(this.input.getA()){
            cam.lookLeft(rotate);
        }else if(this.input.getD()){
            cam.lookLeft(-rotate);
        }*/
    }
    public void cameraUpdate(){
        this.input.update();
        this.setKeyPressed();
        cam.useView();
    }
    
    public InputKey getKeyListener(){
        return this.input;
    }
    
    private void playVideo(){
        int current = this.timer.getDelta();
        if(current > 42150){
            this.close_asteroid = true;
        }else if(current > 20000){
            this.asteroid.getObj().addPosition(0.0f, -0.015f, -0.01f);
            this.asteroid.getObj().addRotation(0.4f, 0.7f, 0.6f);
            this.close_xwing = true;
        }else if(current > 15000){
            this.close_xwing = true;
        }else if(current > 10000){
            this.main_ship.getMissileObj().addPosition(0.01f, 0.0005f, 0f);
        }
    }
}