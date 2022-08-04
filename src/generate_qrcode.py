'''
Aplicaciòn en Flask que busca en la base de datos el còdigo de un punto
y lo despliega en una pagina Html para su lectura
Autores: Gabriel Barròn
Fecha: 11 Julio 2022
'''
import qrcode  #Modulo de codigo qrcode
from PIL import Image  #Modulo para manejo de imagenes
import sys
import getopt

'''
   Metodo de inicio de la aplicacion
'''
def main(argv):
    code = '' #Se inicializa codigo

    try:
      opts, args = getopt.getopt(argv, "c:", ["codigo="])
    except getopt.GetoptError: # Se requiere un codigo
      print('qrcode.py -c <codigo_ a_generar> ')
      sys.exit(2)
    
    if opts is None or len(opts) == 0: # Se requiere un codigo
        print('qrcode.py -c <codigo_ a_generar> ')
        sys.exit(2)

    for opt, arg in opts:
      if opt in ("-c", "--codigo"):
         code = arg

    #Se genera el codigo
    qr = qrcode.QRCode(
        version=1,
        error_correction=qrcode.constants.ERROR_CORRECT_H,
        box_size=10,
        border=4,
    )
    qr.add_data(code)
    qr.make(fit=True)
    img = qr.make_image(fill_color="black", back_color="white").convert('RGB')
    img.save("/home/gbarron/capstone/src/qrcodes/qrcode.png") # se salva el codigo en la ruta

if __name__ == "__main__":
   main(sys.argv[1:])