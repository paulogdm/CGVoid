package main;

import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.core.Material;
import br.usp.icmc.vicg.gl.matrix.Matrix4;
import br.usp.icmc.vicg.gl.model.SolidSphere;
import br.usp.icmc.vicg.gl.util.Shader;
import br.usp.icmc.vicg.gl.util.ShaderFactory;
import br.usp.icmc.vicg.gl.model.Sphere;
import static com.sun.java.accessibility.util.AWTEventMonitor.*;
import java.io.File;
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
import java.util.Vector;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import static java.lang.System.out;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;


public class ObjectBeta implements GLEventListener {
    //bases para o sistema
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
    
    private SimpleObject planet;//nao sei se eh usado
    
    //objetos da cena
    private MainShip main_ship;
    private Planet moon, earth;
    private MainShip landingShip;
    private Point[] points_stars;
    private Asteroid asteroid;
    private ParticleEmitter fireAsteroid;
    private ParticleEmitter fireSpaceShip;
    private ParticleEmitter fireMissile;
    private AsteroidEmitter earth_down;
    
    //ajuda na camera/video
    private float roll;//faz camera girar no final do 'video'
    private final InputKey input;
    private float left_right_angle;
    private float up_down_angle;
    private Timer timer;
    
    //para saber o que mostrar ou nao dado um certo tempo de cena 
    private boolean close_asteroid;
    private boolean close_xwing;
    private boolean close_mainship;
    private boolean start_fireAsteroid;
    private boolean start_fireSpaceShip;
    private boolean start_fireMissile;
    private boolean stop_earth;
    private boolean start_earth_down;
    
    private SolidSphere wave;//nao foi utilizado
    private boolean start_wave;
    
    private boolean shakeLeft;

    private Clip explosion_sound;
    private Clip ship_sound;
    private Clip shipdead_sound;
    private Clip asteroid_sound;
    
    @SuppressWarnings("empty-statement")
    public ObjectBeta() {

        shader = ShaderFactory.getInstance(ShaderFactory.ShaderType.COMPLETE_SHADER);
        modelMatrix = new Matrix4();
        projectionMatrix = new Matrix4();
        cam = new Camera();
        
        light = new Light();
        material = new Material();
        
        main_ship = new MainShip();
        
        moon = new Planet("./data/earth/Moon/moon.obj");
        earth = new Planet("./data/earth/Earth/moon.obj");
        
        points_stars = new Point[5000];//nao foi usado sistema de particulas para fazer as estrelas
        asteroid = new Asteroid("./data/rock/Rock/Rock.obj");
        
        try {
            AudioInputStream audio1 = AudioSystem.getAudioInputStream(new File("./sounds/explosion.wav").getAbsoluteFile());
            this.explosion_sound = AudioSystem.getClip();
            this.explosion_sound.open(audio1);
            
            AudioInputStream audio2 = AudioSystem.getAudioInputStream(new File("./sounds/ship.wav").getAbsoluteFile());
            this.ship_sound = AudioSystem.getClip();
            this.ship_sound.open(audio2);
            
            AudioInputStream audio3 = AudioSystem.getAudioInputStream(new File("./sounds/ship_down.wav").getAbsoluteFile());
            this.shipdead_sound = AudioSystem.getClip();
            this.shipdead_sound.open(audio3);
            
            AudioInputStream audio4 = AudioSystem.getAudioInputStream(new File("./sounds/asteroid.wav").getAbsoluteFile());
            this.asteroid_sound = AudioSystem.getClip();
            this.asteroid_sound.open(audio4);
        } catch (Exception e) {
            System.out.println("play sound error: " + e.getMessage());
        }
    
        landingShip = new MainShip("./data/feisar/Feisar_Ship_OBJ/Feisar_Ship.obj");
        
        input = new InputKey();
        
        left_right_angle = 270;
        up_down_angle = 90;
        
        
        speed_x = speed_y = speed_z = 0.0f;
        rotate_x = rotate_y = rotate_z = 0.0f;
        theta = 45f;
        phi = 45f;
        radius = 1.4142f;
        
        timer = new Timer();
        close_asteroid = false;
        close_xwing = false;
        start_fireAsteroid = false;
        start_fireSpaceShip = false;
        start_fireMissile = false;
        stop_earth = false;
        start_earth_down = false;
        
        wave = new SolidSphere(0.0f, 0.0f, -8.0f);
        start_wave = false;
        shakeLeft = false;
        roll = -15.7f;
       
    }
   
    @Override
    //quando ele começar a desenhar ele vai setar o init
    public void init(GLAutoDrawable glad) {
        GL3 gl = glad.getGL().getGL3();//
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClearDepth(1.0f);
        
//        gl.glDisable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_BLEND);
//        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE);
        
        gl.glEnable(GL.GL_DEPTH_TEST);
        gl.glEnable(GL.GL_CULL_FACE);
        
        shader.init(gl);
        shader.bind();//indica que shader esta ativo   
        
        modelMatrix.init(gl,shader.getUniformLocation("u_modelMatrix"));
        projectionMatrix.init(gl, shader.getUniformLocation("u_projectionMatrix"));
        cam.init(gl, shader.getUniformLocation("u_viewMatrix"));
        this.go = false;
        try {
            //inicializa uma das naves
            main_ship.getObj().getReady(gl, shader);
            main_ship.getObj().addPosition(1.2f, 5.5f, -3.0f);
            main_ship.getObj().addRotation(0, -90.0f, 0);
            
            //inicializa a outra nave
            this.landingShip.getObj().getReady(gl, shader);
            this.landingShip.getObj().setPosition(2.5f,5.5f, -3.0f);
            this.landingShip.getObj().addRotation(0, -90.0f, 0);
            
            //inicializa a terra e a lua
            moon.getObj().getReady(gl, shader);
            moon.changePosition(2.0f, 4.0f, -3.0f);
            earth.getObj().getReady(gl, shader);
            earth.changePosition(0.0f, 0.0f, -8.0f);
            earth.getObj().addRotation(0, 90, 0);
            this.earth.getObj().addSize(2, 2, 2);
            //inicializa o missil(nao eh dado o draw nele)
            main_ship.getMissileObj().getReady(gl, shader);
            main_ship.getMissileObj().addPosition(1.36f, 5.48f, -3.29f);
            main_ship.getMissileObj().addRotation(0.0f, 180, 0.0f);
            
            //inicia o asteroid principal
            asteroid.getObj().getReady(gl, shader);
            asteroid.getObj().addPosition(0.0f, 10.0f, 0.0f);
            asteroid.getObj().addSize(-0.5f, -0.5f, -0.5f);
            
            //inicia a wave(seria uma esfera transparente que faria a camera girar , mas quem faz isso eh a terra)
            //ela inicializa mas nao eh utilizado
            wave.init(gl, shader);
            
            //cria todas as estrelas
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
        
        
        
        /*
            inicializa as particulas do missil atingindo a nave
        */
        Vector location_m = new Vector(3); location_m.add(2.3f/2.0f); location_m.add(5.5f/2.0f); location_m.add(-3f/2.0f);
        float swapingRate_m = 3;//cada update cria 3 particulas
        int particleLifeTime_m = 40;
        Vector gravity_m = new Vector(3); gravity_m.add(0.0f); gravity_m.add(-0.003f); gravity_m.add(0.0f);//para onde as particulas caiem
        Vector initialVelocity_m = new Vector(3); initialVelocity_m.add(0.0f); initialVelocity_m.add(1.0f); initialVelocity_m.add(0.0f);//velocidade das particulas
        float velocityModifier_m = 0.5f;
        this.fireMissile = new ParticleEmitter(location_m, swapingRate_m, particleLifeTime_m, gravity_m, initialVelocity_m, velocityModifier_m, modelMatrix,gl, shader);
        this.fireMissile.setEspaceChange(0.8f, 0.8f, 0.8f);//comecam com 0.5 , deixa 0.8 pq ficou bom :)

        
        /*
            inicia o sistema de particula da nave que vai embora
        */
        Vector location_s = new Vector(3); location_s.add(1.2f/2.0f); location_s.add(5.52f/2.0f); location_s.add(-2.8f/2.0f);
        float swapingRate_s = 3;
        int particleLifeTime_s = 40;
        Vector gravity_s = new Vector(3); gravity_s.add(0.0f); gravity_s.add(-0.0007f); gravity_s.add(0.0f);
        Vector initialVelocity_s = new Vector(3); initialVelocity_s.add(0.0f); initialVelocity_s.add(0.0f); initialVelocity_s.add(0.0f);
        float velocityModifier_s = 1.5f;
        this.fireSpaceShip = new ParticleEmitter(location_s, swapingRate_s, particleLifeTime_s, gravity_s, initialVelocity_s, velocityModifier_s, modelMatrix,gl, shader);
        
        
        /*
            inicia o sistema de particula do asteroid caindo na terra
        */
        Vector location_p = new Vector(3); location_p.add(0.0f); location_p.add(4.9f); location_p.add(-0.1f);
        float swapingRate = 6;
        int particleLifeTime = 50;
        Vector gravity_p = new Vector(3); gravity_p.add(0.0f); gravity_p.add(0f); gravity_p.add(0.2f);
        Vector initialVelocity_p = new Vector(3); initialVelocity_p.add(0.5f); initialVelocity_p.add(0.5f); initialVelocity_p.add(0.0f);
        float velocityModifier = 1.5f;
        this.fireAsteroid = new ParticleEmitter(location_p, swapingRate, particleLifeTime, gravity_p, initialVelocity_p, velocityModifier, modelMatrix,gl, shader);
        this.fireAsteroid.setEspaceChange(0.1f, 0.1f, 0.1f);
        
        /*
            inicia o sistema de asteroid depois que a terra explode
            cria so 50 asteroid e nunc asao deletados(a parte da funcao do update no Asteroid.java foi tirado)
            o tempo de vida de particula nao importa
        */
        Vector location_a = new Vector(3); location_a.add(0.0f); location_a.add(1f); location_a.add(-6f);
        float swapingRate_a = 50;
        int particleLifeTime_a = 50000;
        Vector gravity_a = new Vector(3); gravity_a.add(0.0f); gravity_a.add(-0.001f); gravity_a.add(0.0f);
        Vector initialVelocity_a = new Vector(3); initialVelocity_a.add(0f); initialVelocity_a.add(0f); initialVelocity_a.add(0.0f);
        float velocityModifier_a = 1f;
        this.earth_down = new AsteroidEmitter(location_a, swapingRate_a, particleLifeTime_a, gravity_a, initialVelocity_a, velocityModifier_a, modelMatrix, gl, shader,"./data/rock/Rock/Rock.obj" );
    
        
        /*
            inicializa o tempo depois que TUDO ja foi inicializado
        */
        timer.init();
    }
    
    /*
        cria pontos aleatorios e deixa eles em uma distancia de 7 do ponto 0
    */
    private void create_stars(GL3 gl){
        Random random = new Random();

        for(int i=0; i < this.points_stars.length; i++){            
            float pointx=0.0f, pointy=0.0f, pointz=0.0f;
            while((pointx < 7.0f && pointx > -7.0f) && (pointy < 7.0f && pointy > -7.0f) && (pointz < 7.0f && pointz > -7.0f) ){
                pointx = (random.nextFloat()-0.5f)*50;
                pointy = (random.nextFloat()-0.5f)*50;
                pointz = (random.nextFloat()-0.5f)*50;
            }
            this.points_stars[i] = new Point(pointx, pointy, pointz);
            this.points_stars[i].setPointSize(1.5f, gl);
            this.points_stars[i].init(gl, shader);
        }

    }
    
    private void create_explosion(GL3 gl){
        
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
        
        //atualiza a posicao da camera
        cam.useView();
        light.bind();
        
        //vai pra parte da movimentacao do cenario
        this.playVideo();
        //ver se foi clicado alguma coisa e atualiza a camera(sim repete, nao sei se funfa sem o de cima, good luck)
        this.cameraUpdate();
        
       //vai pra parte em que os objetos sao desenhados novamente
        this.sceneUpdate();
        
        gl.glFlush();
        
        if(this.input.getExit()){
            this.dispose(glad);
        }
        
    }
    
    
    @Override
    //nao tem todos objetos(moh preguica de ver agora)
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
    //ja tava assim quando cheguei
    public void reshape(GLAutoDrawable glad, int i, int i1, int i2, int i3) {
        
    }
    
    public void sceneUpdate(){
        
        /*
            atualiza cada estrela(se nao desenhar elas somem)
        */
        for(int i=0;i<this.points_stars.length; i++){
            this.points_stars[i].draw(modelMatrix, false,material, true);
        }
        
        
        /*
            atualiza a lua, faz ela se mover em 0.07 cada draw novo
        */
        moon.getObj().addRotation(0.0f, 0.07f, 0.0f);
        modelMatrix.loadIdentity();
        modelMatrix.translate(moon.getObj().getPosition()[0], moon.getObj().getPosition()[1], moon.getObj().getPosition()[2]);
        modelMatrix.rotate(moon.getObj().getRotation()[0],1,0,0);
        modelMatrix.rotate(moon.getObj().getRotation()[1],0,1,0);
        modelMatrix.rotate(moon.getObj().getRotation()[2],0,0,1);
        modelMatrix.scale(moon.getObj().getSize()[0], moon.getObj().getSize()[1], moon.getObj().getSize()[2]);
        modelMatrix.bind();
        moon.getObj().draw();
        
        
        /*
            atualiza a terra se ela ainda nao foi explodida)
            gerando-a em 0.03 cada draw novo
            stop_earth eh um booleano alterado em playVideo() na hora certa
        */
        if(!this.stop_earth){
            earth.getObj().addRotation(0.0f, 0.03f, 0.0f);
            modelMatrix.loadIdentity();
            modelMatrix.translate(earth.getObj().getPosition()[0], earth.getObj().getPosition()[1], earth.getObj().getPosition()[2]);
            modelMatrix.rotate(earth.getObj().getRotation()[0],1,0,0);
            modelMatrix.rotate(earth.getObj().getRotation()[1],0,1,0);
            modelMatrix.rotate(earth.getObj().getRotation()[2],0,0,1);
            modelMatrix.scale(earth.getObj().getSize()[0], earth.getObj().getSize()[1], earth.getObj().getSize()[2]);
            modelMatrix.bind();
            earth.getObj().draw();
        }
        
        /*
            atualiza a  nave que vai embora
            close_mainsgip modifica no playVideo() na hora certa
        */
        if(!this.close_mainship){
            modelMatrix.loadIdentity();
            modelMatrix.translate(main_ship.getObj().getPosition()[0], main_ship.getObj().getPosition()[1], main_ship.getObj().getPosition()[2]);
            modelMatrix.rotate(main_ship.getObj().getRotation()[0],1,0,0);
            modelMatrix.rotate(main_ship.getObj().getRotation()[1],0,1,0);
            modelMatrix.rotate(main_ship.getObj().getRotation()[2],0,0,1);
            modelMatrix.scale(main_ship.getObj().getSize()[0], main_ship.getObj().getSize()[1], main_ship.getObj().getSize()[2]);
            modelMatrix.bind();
            main_ship.getObj().draw();
        }
        
        /*
            start_fireSpaceShip eh modifica no playVideo na hora certa
            inicializa o sistema de particulas da nave indo embora
        */
        if(this.start_fireSpaceShip){
            this.fireSpaceShip.update();
            this.fireSpaceShip.draw(material, false);
        }
        
        /*
            some com a nave explodida(sendo q ela nao explode)
            close_xwing vira true dentro do playVideo
        */
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
        
        /*
            asteroid funciona constante e some quando o playVideo alterar o close_asteroid
        */
        if(!this.close_asteroid){
            
            modelMatrix.loadIdentity();
            modelMatrix.translate(asteroid.getObj().getPosition()[0], asteroid.getObj().getPosition()[1], asteroid.getObj().getPosition()[2]);
            modelMatrix.rotate(asteroid.getObj().getRotation()[0],1,0,0);
            modelMatrix.rotate(asteroid.getObj().getRotation()[1],0,1,0);
            modelMatrix.rotate(asteroid.getObj().getRotation()[2],0,0,1);
            modelMatrix.scale(asteroid.getObj().getSize()[0], asteroid.getObj().getSize()[1], asteroid.getObj().getSize()[2]);
            modelMatrix.bind();
            this.asteroid.getObj().draw();
            
        }
        
        /*
            inicializa o sistema de particula
            start_fireAsteroid alterado no playVideo
        */
        if(start_fireAsteroid){
            this.fireAsteroid.update();
            this.fireAsteroid.draw(material, false);
        }
        /*
            inicializa o sistema de particula do missil
        */
        if(start_fireMissile){
            this.fireMissile.update();
            this.fireMissile.draw(material, false);
        }
        
        /*
            quando a terra sumir entao o earth_down(sistema de particulas, varios ateroids) sao cirados
        */
        if(start_earth_down){
            this.earth_down.update();
            this.earth_down.draw(material, false);
        }
        
        /*
            NUNCA VIRA TRUE
            seria uma wave quando a terra explode e a wave cresce e faz a camera girar
            na real nao foi usado por nao conseguir fazer a wave transparente
            entao a terra que cresce e faz a camera girar
        */
        if(start_wave){
            material.setAmbientColor(new float[]{0.0f, 0.0f, 0.0f, 0.3f});
            material.setDiffuseColor(new float[]{0.0f, 0.0f, 1.0f, 0.3f});
            material.setSpecularColor(new float[]{0.3f, 0.3f, 0.3f, 0.3f});
            material.setSpecularExponent(32);
            material.bind();
            modelMatrix.loadIdentity();
            modelMatrix.translate(this.wave.getX(), this.wave.getY(), this.wave.getZ());
            modelMatrix.scale(wave.getSizeX(), wave.getSizeY(), wave.getSizeZ());
            modelMatrix.bind();
            this.wave.bind();
            this.wave.draw();
        }
        
        modelMatrix.loadIdentity();
        modelMatrix.bind();
    }
    
    /*
        Se pa nao eh usado
    */
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
    
    /*
        movimentacao da camera
    */
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
    
    /*
        atualiza a camera 
    */
    public void cameraUpdate(){
        this.input.update();
        this.setKeyPressed();
        cam.useView();
    }
    
    public InputKey getKeyListener(){
        return this.input;
    }
    
    /*
        funcao que faz com que tenha um video enquando o programa funciona
    */
    private void playVideo(){
        int current = this.timer.getDelta();
        if(current > 39650){//faz a camera girar ate parar e ja era
            this.close_asteroid = true;
            this.stop_earth = true;
            this.start_fireAsteroid = false;
            //this.rollCam();
        }else if(current > 39100){//unica coisa que adicona eh a camera q comeca a girar
            this.start_earth_down = true;
            this.asteroid.getObj().addPosition(0.0f, -0.015f, -0.01f);
            this.asteroid.getObj().addRotation(0.4f, 0.7f, 0.6f);
            this.fireAsteroid.setLocation(0.00f, -(0.015f/2.0f), -(0.01f/2.0f));
            //wave comeca a agir NEM VAI
            this.earth.getObj().addSize(0.35f, 0.35f, 0.35f);
            //this.start_wave = true;
            //this.wave.addSize(0.3f,0.3f,0.3f);
            //this.rollCam();//gira camera
        }else if(current > 38000){ // pedacos da terra sao soltos
            this.start_earth_down = true;
            this.asteroid.getObj().addPosition(0.0f, -0.015f, -0.01f);
            this.asteroid.getObj().addRotation(0.4f, 0.7f, 0.6f);
            this.fireAsteroid.setLocation(0.00f, -(0.015f/2.0f), -(0.01f/2.0f));
            //wave comeca a agir NEM FUNCIONA
           // this.start_wave = true;
            this.earth.getObj().addSize(0.3f, 0.3f, 0.3f);
            //this.wave.addSize(0.3f,0.3f,0.3f);
            this.shakeCam();
            this.explosion_sound.start();
            this.asteroid_sound.stop();
        }else if(current > 20000){//nave some, asteroid comeca a ir para a terra
            this.asteroid.getObj().addPosition(0.0f, -0.015f, -0.01f);
            this.asteroid.getObj().addRotation(0.4f, 0.7f, 0.6f);
            this.start_fireAsteroid = true;
            this.fireAsteroid.setLocation(0.00f, -(0.015f/2.0f), -(0.01f/2.0f));
            this.close_mainship = true;
            this.start_fireSpaceShip = false;
            this.asteroid_sound.start();
        }else if(current > 18000){//nave vai embora
            this.start_fireSpaceShip = true;
            this.main_ship.getObj().addPosition(0.0f, 0.0f, -0.2f);
            this.fireSpaceShip.setLocation(0.0f, 0.0f, -(0.2f/2.0f));
            this.ship_sound.start();
        }else if(current > 15000){//nave rotaciona
            this.main_ship.getObj().addRotation(0f, 1f, 0f);
        }else if(current > 14000){//cancela a nave atingida, o missil e as faiscas
            this.close_xwing = true;
            this.start_fireMissile = false;
            this.shipdead_sound.start();
        }else if(current > 12500){ //missel encosta na outra nave e sai faisca
            this.main_ship.getMissileObj().addPosition(0.01f, 0.0005f, 0f);
            this.fireMissile.setLocation(0.01f/2.0f, 0.0005f/2.0f, 0.0f);
            this.start_fireMissile = true;
        }else if(current > 10000){ //missil eh desparado
            this.main_ship.getMissileObj().addPosition(0.01f, 0.0005f, 0f);
        }
    }
    
    //treme a camera antes da explosao
    private void shakeCam(){
        if(this.shakeLeft){
            cam.lookLeft(2f);
        }else{
            cam.lookLeft(-2f);
        }
        this.shakeLeft = !this.shakeLeft;
    }
    
    //gira a camera com a explosao
    private void rollCam(){
        if(this.roll < 0){
            cam.lookDown(this.roll);
            this.roll += 0.05;
        }
    }
}